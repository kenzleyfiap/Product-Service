package br.com.kenzley.fiap.service.product.business;

import br.com.kenzley.fiap.service.product.api.converter.ProductConverter;
import br.com.kenzley.fiap.service.product.api.converter.ProductMapper;
import br.com.kenzley.fiap.service.product.api.request.ProductRequestDTO;
import br.com.kenzley.fiap.service.product.api.response.ProductResponseDTO;
import br.com.kenzley.fiap.service.product.infraescruture.entity.ProductEntity;
import br.com.kenzley.fiap.service.product.infraescruture.exceptions.BusinessException;
import br.com.kenzley.fiap.service.product.infraescruture.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static org.springframework.util.Assert.notNull;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductConverter productConverter;
    private final ProductMapper productMapper;
    private final CategoryService categoryService;


    public ProductEntity saveProduct(ProductEntity productEntity) {
        return productRepository.save(productEntity);
    }

    public ProductResponseDTO engraveProducts(ProductRequestDTO productRequestDTO) {

        try {
            notNull(productRequestDTO, "Product data is mandatory");
            var productEntity = saveProduct(productConverter.toProductEntity(productRequestDTO));
            var categoryEntity = categoryService.saveCategory(
                    productConverter.toCategoryEntity(productRequestDTO.getCategory(), productEntity.getId()));
            return productMapper.toProductResponseDTO(productEntity, categoryEntity);
        } catch (Exception e) {
            throw new BusinessException("Error writing product data", e);
        }
    }

}
