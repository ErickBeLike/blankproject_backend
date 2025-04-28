package com.application.blank.service.file;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Implementación del servicio de almacenamiento de archivos en el sistema de archivos local.
 *
 * @author Misrael Florentino
 * @credit Agradecimiento especial a Misrael Florentino por su valiosa contribución
 *         en el desarrollo e implementación de este servicio de almacenamiento.
 */

@Service
public class FileSystemStorageService implements StorageService {

    private static final Logger log = LoggerFactory.getLogger(FileSystemStorageService.class);

    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");
    private static final long MAX_FILE_SIZE = 100 * 1024 * 1024;

    @Value("${media.location.base}")
    private String baseLocation;

    private Path rootLocation;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void init() throws IOException {
        rootLocation = Paths.get(baseLocation).toAbsolutePath().normalize();
        log.info("Inicializando almacenamiento en: {}", rootLocation);

        try {
            Files.createDirectories(rootLocation);
            log.info("Directorio base creado/verificado: {}", rootLocation);

            // Verificar permisos
            if (!Files.isWritable(rootLocation) || !Files.isReadable(rootLocation)) {
                throw new RuntimeException("El directorio base no tiene permisos de lectura/escritura");
            }
        } catch (IOException e) {
            log.error("Error al inicializar el almacenamiento: {}", e.getMessage());
            throw new RuntimeException("No se pudo inicializar el almacenamiento", e);
        }
    }

    private void verifyStorageTask() {
        try {
            verifyStorage();
        } catch (Exception e) {
            log.error("Error en la verificación periódica del almacenamiento: {}", e.getMessage());
        }
    }

    @Override
    public boolean verifyStorage() {
        try {
            if (!Files.exists(rootLocation)) {
                log.warn("Directorio base de almacenamiento no existe, creándolo...");
                Files.createDirectories(rootLocation);
            }

            // Verificar permisos
            boolean isWritable = Files.isWritable(rootLocation);
            boolean isReadable = Files.isReadable(rootLocation);

            log.info("Estado del almacenamiento - Path: {}, Existe: true, Escribible: {}, Legible: {}",
                    rootLocation, isWritable, isReadable);

            if (!isWritable || !isReadable) {
                log.error("Problemas con los permisos del directorio de almacenamiento");
                return false;
            }

            // Crear archivo de prueba
            Path testFile = rootLocation.resolve(".test");
            Files.writeString(testFile, "test");
            boolean testSuccess = Files.deleteIfExists(testFile);

            if (!testSuccess) {
                log.error("No se pudo crear/eliminar archivo de prueba");
                return false;
            }

            return true;
        } catch (IOException e) {
            log.error("Error al verificar el almacenamiento: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public String saveFile(MultipartFile file, String filename, String subfolder) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("No se puede almacenar un archivo vacío");
            }

            if (!isImageFile(file)) {
                throw new RuntimeException("Tipo de archivo no permitido. Solo se permiten imágenes (jpg, jpeg, png, gif)");
            }

            if (file.getSize() > MAX_FILE_SIZE) {
                throw new RuntimeException("El archivo excede el tamaño máximo permitido de 4MB");
            }

            // Verificar almacenamiento antes de guardar
            if (!verifyStorage()) {
                throw new RuntimeException("El sistema de almacenamiento no está disponible");
            }

            // Determinar ruta para la subcarpeta
            Path subfolderPath = rootLocation.resolve(subfolder).normalize();
            if (!Files.exists(subfolderPath)) {
                Files.createDirectories(subfolderPath); // Crear la subcarpeta si no existe
            }

            // Generar nombre de archivo único
            String uniqueFilename = generateUniqueFilename(filename);
            Path destinationFile = subfolderPath.resolve(uniqueFilename).normalize().toAbsolutePath();

            if (!destinationFile.getParent().equals(subfolderPath)) {
                throw new RuntimeException("No se puede almacenar el archivo fuera del directorio destino");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                log.info("Archivo guardado exitosamente en: {}", destinationFile);

                // Verificar que el archivo se guardó correctamente
                if (!Files.exists(destinationFile)) {
                    throw new RuntimeException("El archivo no se guardó correctamente");
                }

                return uniqueFilename;
            }

        } catch (IOException e) {
            log.error("Error al guardar el archivo {}", filename, e);
            throw new RuntimeException("Error al almacenar el archivo " + filename, e);
        }
    }

    @Override
    public Resource loadAsResource(String filename, String subfolder) {
        try {
            // Determinar la ruta del archivo dentro de la subcarpeta
            Path file = rootLocation.resolve(subfolder).resolve(filename).normalize();
            log.info("Intentando cargar el archivo: {}", file.toAbsolutePath());

            if (!file.toAbsolutePath().startsWith(rootLocation)) {
                log.error("Intento de acceso a archivo fuera del directorio raíz: {}", file);
                throw new RuntimeException("No se puede leer archivo fuera del directorio raíz");
            }

            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                log.error("Archivo no encontrado o no legible: {}", filename);
                throw new RuntimeException("Archivo no encontrado: " + filename);
            }
        } catch (IOException e) {
            log.error("Error al cargar archivo {}: {}", filename, e.getMessage());
            throw new RuntimeException("No se pudo leer el archivo: " + filename, e);
        }
    }

    @Override
    public void deleteFile(String filename, String subfolder) {
        try {
            // Determinar la ruta del archivo dentro de la subcarpeta
            Path file = rootLocation.resolve(subfolder).resolve(filename).normalize().toAbsolutePath();

            if (!file.startsWith(rootLocation)) {
                throw new RuntimeException("No se puede eliminar archivo fuera del directorio raíz");
            }

            if (Files.deleteIfExists(file)) {
                log.info("Archivo eliminado exitosamente: {}", filename);
            } else {
                log.warn("El archivo {} no existe", filename);
            }
        } catch (IOException e) {
            log.error("Error al eliminar archivo {}: {}", filename, e.getMessage());
            throw new RuntimeException("No se pudo eliminar el archivo: " + filename, e);
        }
    }

    @Override
    public String generateUniqueFilename(String originalFilename) {
        String extension = FilenameUtils.getExtension(originalFilename);
        return UUID.randomUUID().toString() + "." + extension;
    }

    @Override
    public List<String> saveFiles(List<MultipartFile> files, String subfolder) {
        List<String> savedFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                String savedFilename = saveFile(file, file.getOriginalFilename(), subfolder);
                savedFiles.add(savedFilename);
            } catch (Exception e) {
                // Si hay un error al guardar un archivo, eliminar los archivos ya guardados
                savedFiles.forEach(filename -> deleteFile(filename, subfolder));
                throw new RuntimeException("Error al guardar los archivos", e);
            }
        }

        return savedFiles;
    }

    @Override
    public boolean isImageFile(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        return extension != null && ALLOWED_IMAGE_EXTENSIONS.contains(extension.toLowerCase());
    }
}
