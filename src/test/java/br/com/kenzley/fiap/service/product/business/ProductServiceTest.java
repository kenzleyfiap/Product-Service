package br.com.kenzley.fiap.service.product.business;

import br.com.kenzley.fiap.service.product.api.converter.ProductConverter;
import br.com.kenzley.fiap.service.product.api.converter.ProductMapper;
import br.com.kenzley.fiap.service.product.api.request.CategoryRequestDTO;
import br.com.kenzley.fiap.service.product.api.request.ProductRequestDTO;
import br.com.kenzley.fiap.service.product.api.response.ProductResponseDTO;
import br.com.kenzley.fiap.service.product.infrastructure.entity.CategoryEntity;
import br.com.kenzley.fiap.service.product.infrastructure.entity.ProductEntity;
import br.com.kenzley.fiap.service.product.infrastructure.exceptions.BusinessException;
import br.com.kenzley.fiap.service.product.infrastructure.repository.ProductRepository;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.kenzley.fiap.service.product.utils.ProductHelper.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductConverter productConverter;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private CategoryService categoryService;
    private ProductService productService;
    AutoCloseable mock;

    @BeforeEach
    void setup(){
        mock = MockitoAnnotations.openMocks(this);
        productService = new ProductService(
                productRepository,
                productConverter,
                productMapper,
                categoryService
        );
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    void mustAllowRegisterProduct() {

        // Arrange
        var product = gerarProductEntity();
        when(productRepository.save(any(ProductEntity.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        var productRegistred = productService.saveProduct(product);

        // Assert
        assertThat(productRegistred).isInstanceOf(ProductEntity.class).isNotNull();

        assertThat(productRegistred.getName()).isEqualTo(product.getName());
        assertThat(productRegistred.getPrice()).isEqualTo(product.getPrice());
        assertThat(productRegistred.getInformation()).isEqualTo(product.getInformation());

    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    void mustAllowEngraveProduct() {

        // Arrange
        var productRequestDTO = gerarProductRequest();
        when(productRepository.save(any(ProductEntity.class))).thenReturn(gerarProductEntityComId());
        when(categoryService.saveCategory(any(CategoryEntity.class))).thenAnswer(i -> i.getArgument(0));
        when(productService.saveProduct(any(ProductEntity.class))).thenReturn(gerarProductEntityComId());
        when(productConverter.toProductEntity(any(ProductRequestDTO.class))).thenReturn(gerarProductEntityComId());
        when(productConverter.toCategoryEntity(any(CategoryRequestDTO.class), any(String.class))).thenReturn(gerarCategoryEntity());
        when(productMapper.toProductResponseDTO(any(ProductEntity.class), any(CategoryEntity.class))).thenReturn(gerarProductResponse());


        // Act
        var productRegistred = productService.engraveProducts(productRequestDTO);

        // Assert
        assertThat(productRegistred).isInstanceOf(ProductResponseDTO.class).isNotNull();

        assertThat(productRegistred.name()).isEqualTo(productRequestDTO.getName());
        assertThat(productRegistred.price()).isEqualTo(productRequestDTO.getPrice());
        assertThat(productRegistred.information()).isEqualTo(productRequestDTO.getInformation());

    }

    @Test
    void mustGenerateExceptionWhenCategoryNull() {
        ProductRequestDTO productRequestDTO = gerarProductRequest();
        productRequestDTO.setCategory(null);

        assertThatThrownBy(() -> productService.engraveProducts(productRequestDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Error writing product data");
    }

    @Test
    void mustAllowFindProduct() {
        // Arrange
        var id = UUID.randomUUID().toString();
        when(productRepository.findById(any(String.class))).thenReturn(Optional.of(gerarProductEntityComId()));
        when(categoryService.findByProductId(any(String.class))).thenReturn(gerarCategoryEntity());
        when(productMapper.toProductResponseDTO(any(ProductEntity.class), any(CategoryEntity.class))).thenReturn(gerarProductResponse());

        // Act
        ProductResponseDTO productReceived = productService.findById(gerarProductEntityComId().getId());

        // Assert
        Assertions.assertThat(gerarProductEntityComId().getName()).isEqualTo(productReceived.name());
        verify(productRepository, times(1)).findById(any(String.class));

    }

    @Test
    void mustGenerateExceptionWhenIdDoesNotExist() {
        var id = UUID.randomUUID().toString();

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(id))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Product not found");

        verify(productRepository, times(1)).findById(id);
    }

    @Test
    void mustAllowUpdateProduct() {
        var id = UUID.randomUUID().toString();
        var oldProduct = gerarProductEntity();
        oldProduct.setId(id);

        var newProduct = new ProductEntity();
        newProduct.setId(oldProduct.getId());
        newProduct.setName("teste");
        newProduct.setInformation("teste");

        when(productRepository.findById(id)).thenReturn(Optional.of(oldProduct));
        when(productRepository.save(any(ProductEntity.class))).thenAnswer(i -> i.getArgument(0));
        when(productConverter.toProductEntity(any(ProductRequestDTO.class))).thenReturn(newProduct);
        when(categoryService.findByProductId(id)).thenReturn(gerarCategoryEntity());
        when(productMapper.toProductResponseDTO(any(ProductEntity.class), any(CategoryEntity.class))).thenReturn(gerarProductResponse());

        // act
        ProductResponseDTO productReceived = productService.updateById(id, gerarProductRequest());

        // assert

        assertThat(productReceived).isInstanceOf(ProductResponseDTO.class);
        assertThat(productReceived).isNotNull();

        verify(productRepository, times(1)).findById(any(String.class));
        verify(productRepository, times(1)).save(any(ProductEntity.class));
    }


    @Test
    void mustGenerateExceptionWhenIdDoesNotExistInUpdate() {
        var id = UUID.randomUUID().toString();

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.updateById(id, gerarProductRequest()))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Product not found");

        verify(productRepository, times(1)).findById(id);
    }

    @Test
    void mustAllowRemoveProduct() {
        var id = UUID.randomUUID().toString();
        var product = gerarProductEntity();

        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).deleteById(id);

        productService.deleteById(id);

        verify(productRepository, times(1)).findById(any(String.class));
        verify(productRepository, times(1)).deleteById(any(String.class));
    }

    @Test
    void mustGenerateExceptionProductNotFoundInDelete() {
        var id = UUID.randomUUID().toString();

        assertThatThrownBy(() -> productService.deleteById(id))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Product not found");

        verify(productRepository, times(1)).findById(id);
    }


    @Test
    void mustAllowListProducts() {
        ProductEntity product1 = gerarProductEntity();
        ProductEntity product2 = gerarProductEntity();

        List<ProductEntity> listProducts = Arrays.asList(product1, product2);

        when(productRepository.findAll()).thenReturn(listProducts);

        // Act
        var productsReceived = productService.findAll();

        // Assert
        Assertions.assertThat(productsReceived).hasSize(2);

        verify(productRepository, times(1)).findAll();
    }


}