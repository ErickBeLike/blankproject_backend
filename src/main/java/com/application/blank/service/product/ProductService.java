package com.application.blank.service.product;

import com.application.blank.dto.product.ProductDTO;
import com.application.blank.entity.product.Product;
import com.application.blank.entity.product.ProductCategory;
import com.application.blank.exception.ResourceNotFoundException;
import com.application.blank.mapper.product.ProductMapper;
import com.application.blank.repository.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired private ProductRepository productRepository;
    @Autowired private ProductMapper productMapper;

    // Get all products
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Get product by ID
    public ProductDTO getProductById(Long id) throws ResourceNotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for ID: " + id));
        return productMapper.toDTO(product);
    }

    // Create new product
    @Transactional
    public ProductDTO saveProduct(ProductDTO dto) {
        Product product = productMapper.toEntity(dto);

        // Set stock to 0 if it is null
        if (product.getStock() == 0) {
            product.setStock(0);
        }

        // Save product
        Product savedProduct = productRepository.save(product);
        return productMapper.toDTO(savedProduct);
    }

    // Update existing product
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO dto) throws ResourceNotFoundException {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for ID: " + id));

        // Update product fields
        if (dto.getProductName() != null) {
            existingProduct.setProductName(dto.getProductName());
        }
        if (dto.getProductDescription() != null) {
            existingProduct.setProductDescription(dto.getProductDescription());
        }
        if (dto.getProductCategory() != null) {
            existingProduct.setProductCategory(dto.getProductCategory());
        }
        // Si dto.getStock() es 0, se asigna 0 al stock
        if (dto.getStock() != 0) {
            existingProduct.setStock(dto.getStock());
        } else {
            existingProduct.setStock(0);  // Asignamos 0 si el valor es 0
        }
        if (dto.getProductPrice() != null) {
            existingProduct.setProductPrice(dto.getProductPrice().intValue());
        }

        // Save updated product
        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toDTO(updatedProduct);
    }

    // Delete product
    @Transactional
    public Map<String, Boolean> deleteProduct(Long id) throws ResourceNotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for ID: " + id));

        productRepository.delete(product);
        return Collections.singletonMap("deleted", Boolean.TRUE);
    }
}
