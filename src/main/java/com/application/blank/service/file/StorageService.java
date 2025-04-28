package com.application.blank.service.file;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Interfaz para el servicio de almacenamiento de archivos.
 * Proporciona métodos para guardar, cargar y gestionar archivos en el sistema.
 *
 * @author Misrael Florentino
 * @credit Agradecimiento especial a Misrael Florentino por su contribución al desarrollo de este módulo.
 */

public interface StorageService {

    /**
     * Inicializa el servicio de almacenamiento, creando los directorios necesarios.
     * @throws IOException Si ocurre un error al crear los directorios.
     */
    void init() throws IOException;

    /**
     * Guarda un archivo dentro de una subcarpeta específica.
     * @param file El archivo a guardar.
     * @param filename El nombre del archivo.
     * @param subfolder La subcarpeta donde se guardará el archivo.
     * @return El nombre único del archivo guardado.
     */
    String saveFile(MultipartFile file, String filename, String subfolder);

    /**
     * Carga un archivo como recurso dentro de una subcarpeta específica.
     * @param filename El nombre del archivo a cargar.
     * @param subfolder La subcarpeta donde se encuentra el archivo.
     * @return El archivo como recurso.
     */
    Resource loadAsResource(String filename, String subfolder);

    /**
     * Elimina un archivo de una subcarpeta específica.
     * @param filename El nombre del archivo a eliminar.
     * @param subfolder La subcarpeta donde se encuentra el archivo.
     */
    void deleteFile(String filename, String subfolder);

    /**
     * Genera un nombre de archivo único.
     * @param originalFilename El nombre original del archivo.
     * @return Un nombre único para el archivo.
     */
    String generateUniqueFilename(String originalFilename);

    /**
     * Guarda una lista de archivos dentro de una subcarpeta específica.
     * @param files Lista de archivos a guardar.
     * @param subfolder La subcarpeta donde se guardarán los archivos.
     * @return Lista de los nombres únicos de los archivos guardados.
     */
    List<String> saveFiles(List<MultipartFile> files, String subfolder);

    /**
     * Verifica si el archivo es un tipo de imagen permitido (jpg, jpeg, png, gif).
     * @param file El archivo a verificar.
     * @return true si el archivo es una imagen válida.
     */
    boolean isImageFile(MultipartFile file);

    /**
     * Verifica que el almacenamiento esté en buen estado y que los permisos sean correctos.
     * @return true si el almacenamiento está listo.
     */
    boolean verifyStorage();
}
