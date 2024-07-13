package br.com.kenzley.fiap.service.product.api;

import br.com.kenzley.fiap.service.product.api.converter.ProductConverter;
import br.com.kenzley.fiap.service.product.api.converter.ProductMapper;
import br.com.kenzley.fiap.service.product.api.request.ProductRequestDTO;
import br.com.kenzley.fiap.service.product.api.response.ProductResponseDTO;
import br.com.kenzley.fiap.service.product.business.CategoryService;
import br.com.kenzley.fiap.service.product.business.ProductService;
import br.com.kenzley.fiap.service.product.infrastructure.entity.ProductEntity;
import br.com.kenzley.fiap.service.product.infrastructure.exceptions.ProductNotFoundException;
import br.com.kenzley.fiap.service.product.infrastructure.exceptions.handler.GlobalExceptionHandler;
import br.com.kenzley.fiap.service.product.utils.ProductHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static br.com.kenzley.fiap.service.product.utils.ProductHelper.gerarProductRequest;
import static br.com.kenzley.fiap.service.product.utils.ProductHelper.gerarProductResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ProductControllerTest {

    private MockMvc mockMvc;
    @Mock
    private ProductService productService;
    @Mock
    private ProductConverter productConverter;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private CategoryService categoryService;
    AutoCloseable mock;

    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);
        ProductController productController = new ProductController(productService);
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .addFilter((request,response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                })
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }


    @Nested
    class RegisterProduct {

        @Test
        void mustAllowRegisterProduct() throws Exception {

            // Arrange
            var product = ProductHelper.gerarProductRequest();
            when(productService.engraveProducts(any(ProductRequestDTO.class))).thenAnswer(i -> i.getArgument(0));
            when(productService.saveProduct(any(ProductEntity.class))).thenAnswer(i -> i.getArgument(0));
            when(productService.engraveProducts(any(ProductRequestDTO.class))).thenReturn(gerarProductResponse());

            // Act && Assert
            mockMvc.perform(
                    post("/product")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(product))
            ).andExpect(status().isOk());

            verify(productService, times(1)).engraveProducts(any(ProductRequestDTO.class));



        }

        @Test
        void mustGenerateExceptionWhenRegisterProductPayloadXML() throws Exception {
            String xmlPayload = "<productRequestDTO><name>hamburguer</name></productRequestDTO>";

            mockMvc.perform(post("/product").contentType(MediaType.APPLICATION_XML)
                            .content(xmlPayload))
                    .andExpect(status().isUnsupportedMediaType());

            verify(productService, never()).engraveProducts(any(ProductRequestDTO.class));
        }
    }

    @Nested
    class FindProduct {

        @Test
        void mustAllowFindProduct() throws Exception {

            var id = UUID.fromString("93196cfa-7180-4493-be67-b36cfcd7c18f").toString();
            var product = gerarProductResponse();
            when(productService.findById(any(String.class)))
                    .thenReturn(product);

            mockMvc.perform(get("/product/{id}", id)).andExpect(status().isOk());

            verify(productService, times(1)).findById(any(String.class));

        }

        @Test
        void mustGenerateExceptionWhenIdNotFound() throws Exception {
            var id = UUID.fromString("c8f20675-89a9-44a6-8c39-fdc61a817822").toString();
            var product = gerarProductRequest();

            when(productService.findById(id))
                    .thenThrow(new ProductNotFoundException("Product not Found"));

            mockMvc.perform(get("/product/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(product)))
                    .andExpect(status().isNotFound());

            verify(productService, times(1)).findById(any(String.class));
        }

    }

    @Nested
    class UpdateProduct {

        @Test
        void mustAllowUpdateProduct() throws Exception {
            var id = UUID.fromString("77effc7a-3a71-4751-a045-36a8598dbb3a").toString();
            var product = gerarProductRequest();


            when(productService.updateById(id, product))
                    .thenReturn(gerarProductResponse());


            mockMvc.perform(put("/product/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(product)))
                    .andExpect(status().isOk());

            verify(productService, times(1)).updateById(id, product);
        }

        @Test
        void mustGenerateExceptionWhenUpdateProductPayloadXML() throws Exception {
            String xmlPayload = "<productRequestDTO><name>hamburguer</name></productRequestDTO>";
            var id = UUID.fromString("77effc7a-3a71-4751-a045-36a8598dbb3a");

            mockMvc.perform(put("/product/{id}", id)
                            .contentType(MediaType.APPLICATION_XML)
                            .content(xmlPayload))
                    .andExpect(status().isUnsupportedMediaType());

            verify(productService, never()).updateById(any(String.class), any(ProductRequestDTO.class));
        }


        @Test
        void mustGenerateExceptionWhenIdNotFound() throws Exception {
            var id = UUID.fromString("c8f20675-89a9-44a6-8c39-fdc61a817822").toString();
            var product = gerarProductRequest();

            when(productService.updateById(id, product))
                    .thenThrow(new ProductNotFoundException("Product not Found"));

            mockMvc.perform(put("/product/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(product)))
                    .andExpect(status().isNotFound());

            verify(productService, times(1)).updateById(any(String.class), any(ProductRequestDTO.class));
        }

    }

    @Nested
    class RemoveProduct {

        @Test
        void mustAllowRemoveProduct() throws Exception {
            var id = UUID.fromString("df0206c1-cf4e-44ec-b670-40ccf899e3eb").toString();

            doNothing().when(productService).deleteById(id);
            when(productService.findById(id)).thenReturn(gerarProductResponse());


            mockMvc.perform(delete("/product/{id}", id))
                    .andExpect(status().isNoContent());

            verify(productService, times(1)).deleteById(id);
        }

    }

    @Nested
    class ListProducts {

        @Test
        void mustAllowListProducts() throws Exception {
            var product = gerarProductResponse();

            List<ProductResponseDTO> products = List.of(product, product);

            when(productService.findAll()).thenReturn(products);

            mockMvc.perform(get("/product"))
                    .andExpect(status().isOk());
        }

    }


    public static String asJsonString(final Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        return objectMapper.writeValueAsString(object);
    }

}