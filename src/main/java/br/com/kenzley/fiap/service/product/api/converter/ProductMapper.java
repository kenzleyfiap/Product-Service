package br.com.kenzley.fiap.service.product.api.converter;

import br.com.kenzley.fiap.service.product.api.response.ProductResponseDTO;
import br.com.kenzley.fiap.service.product.infraescruture.entity.CategoryEntity;
import br.com.kenzley.fiap.service.product.infraescruture.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "id", source = "product.id")
    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "price", source = "product.price")
    @Mapping(target = "information", source = "product.information")
    @Mapping(target = "category", source = "categoryEntity")
    ProductResponseDTO toProductResponseDTO(ProductEntity product, CategoryEntity categoryEntity);
}
