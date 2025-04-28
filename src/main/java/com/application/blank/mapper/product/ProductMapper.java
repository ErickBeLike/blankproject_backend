package com.application.blank.mapper.product;

import com.application.blank.dto.product.ProductDTO;
import com.application.blank.entity.product.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    // Convert Entity to DTO
    public ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setProductDescription(product.getProductDescription());
        dto.setProductCategory(product.getProductCategory());
        dto.setStock(product.getStock());
        dto.setProductPrice((double) product.getProductPrice());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        return dto;
    }

    // Convert DTO to Entity
    public Product toEntity(ProductDTO dto) {
        Product product = new Product();
        product.setProductName(dto.getProductName());
        product.setProductDescription(dto.getProductDescription());
        product.setProductCategory(dto.getProductCategory());
        product.setStock(dto.getStock()); // Nunca será null
        product.setProductPrice(dto.getProductPrice().intValue());
        return product;
    }
}
