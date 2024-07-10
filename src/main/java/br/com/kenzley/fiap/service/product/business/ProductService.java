package br.com.kenzley.fiap.service.product.business;

import br.com.kenzley.fiap.service.product.api.converter.ProductConverter;
import br.com.kenzley.fiap.service.product.api.converter.ProductMapper;
import br.com.kenzley.fiap.service.product.api.request.ProductRequestDTO;
import br.com.kenzley.fiap.service.product.api.response.ProductResponseDTO;
import br.com.kenzley.fiap.service.product.infrastructure.entity.CategoryEntity;
import br.com.kenzley.fiap.service.product.infrastructure.entity.ProductEntity;
import br.com.kenzley.fiap.service.product.infrastructure.exceptions.BusinessException;
import br.com.kenzley.fiap.service.product.infrastructure.exceptions.ProductNotFoundException;
import br.com.kenzley.fiap.service.product.infrastructure.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<ProductResponseDTO> findAll() {
        List<ProductEntity> products = productRepository.findAll();

        return products.stream()
                .map(product -> {
                    CategoryEntity category = categoryService.findByProductId(product.getId());
                    return productMapper.toProductResponseDTO(product, category);
                })
                .toList();
    }

    public ProductResponseDTO findById(String id) {
        var product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
        var category = categoryService.findByProductId(product.getId());
        return productMapper.toProductResponseDTO(product, category);
    }

    public ProductResponseDTO updateById(String id, ProductRequestDTO productRequestDTO) {
        findById(id);
        var productEntity = productConverter.toProductEntity(productRequestDTO);
        var category = categoryService.findByProductId(id);
        productEntity.setId(id);

        return productMapper.toProductResponseDTO(productRepository.save(productEntity), category);
    }

    public void deleteById(String id) {
        findById(id);
        productRepository.deleteById(id);
        categoryService.deleteByProductId(id);
    }
}
