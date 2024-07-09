package br.com.kenzley.fiap.service.product.infrastructure.repository;

import br.com.kenzley.fiap.service.product.infrastructure.entity.ProductEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static br.com.kenzley.fiap.service.product.utils.ProductHelper.gerarProductEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class ProductRepositoryTest {

    @Mock
    private ProductRepository productRepository;

    AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void mustAllowRegisterProduct() {
       // Arrange
        var product = gerarProductEntity();
        when(productRepository.save(any(ProductEntity.class))).thenReturn(product);

        // Act
        ProductEntity productRegistred = productRepository.save(product);

        // Assert

        assertThat(productRegistred)
                .isNotNull()
                .isEqualTo(product);

        verify(productRepository, times(1)).save(any(ProductEntity.class));
    }

    @Test
    void mustAllowFindProduct() {

        // Arrange

        var id = UUID.randomUUID();
        var product = gerarProductEntity();
        product.setId(id.toString());

        when(productRepository.findById(any(String.class)))
                .thenReturn(Optional.of(product));

        // Act
        var productReceivedOptional = productRepository.findById(id.toString());

        // Assert

        assertThat(productReceivedOptional)
                .isPresent()
                .containsSame(product);


        productReceivedOptional.ifPresent(productReceived -> {
            assertThat(productReceived.getId()).isEqualTo(product.getId());
            assertThat(productReceived.getName()).isEqualTo(product.getName());
            assertThat(productReceived.getInformation()).isEqualTo(product.getInformation());
        });

        verify(productRepository, times(1)).findById(any(String.class));
    }

    @Test
    void mustAllowDeleteProduct() {
        //Arrange
        var id = UUID.randomUUID();
        doNothing().when(productRepository).deleteById(any(String.class));

        // Act
        productRepository.deleteById(id.toString());

        // Assert
        verify(productRepository, times(1)).deleteById(any(String.class));

    }

    @Test
    void mustAllowFindAllProducts() {
        var product1 = gerarProductEntity();
        var product2 = gerarProductEntity();

        // Arrange
        var productList = Arrays.asList(
                product1,
                product2
        );

        when(productRepository.findAll()).thenReturn(productList);

        //Act
        var productsListReceived = productRepository.findAll();

        //Assert
        assertThat(productsListReceived)
                .hasSize(2)
                .containsExactlyInAnyOrder(product1, product2);

        verify(productRepository, times(1)).findAll();
    }


}