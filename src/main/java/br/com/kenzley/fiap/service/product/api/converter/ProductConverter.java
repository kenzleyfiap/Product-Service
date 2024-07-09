package br.com.kenzley.fiap.service.product.api.converter;

import br.com.kenzley.fiap.service.product.api.request.CategoryRequestDTO;
import br.com.kenzley.fiap.service.product.api.request.ProductRequestDTO;
import br.com.kenzley.fiap.service.product.infraescruture.entity.CategoryEntity;
import br.com.kenzley.fiap.service.product.infraescruture.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductConverter {


    public ProductEntity toProductEntity(ProductRequestDTO productDTO) {
        return ProductEntity.builder()
                .id(UUID.randomUUID().toString())
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .information(productDTO.getInformation())
                .build();
    }


    public CategoryEntity toCategoryEntity(CategoryRequestDTO categoryDTO, String productId) {
        return CategoryEntity.builder()
                .productId(productId)
                .name(categoryDTO.getName())
                .build();
    }

}
