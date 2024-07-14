package br.com.kenzley.fiap.service.product.api;

import br.com.kenzley.fiap.service.product.api.request.ProductRequestDTO;
import br.com.kenzley.fiap.service.product.api.response.ProductResponseDTO;
import br.com.kenzley.fiap.service.product.business.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ProductResponseDTO> engraveProducts(@RequestBody ProductRequestDTO productRequestDTO) {
        return ResponseEntity.ok(productService.engraveProducts(productRequestDTO));
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<ProductResponseDTO>> findAllProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable String id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ProductResponseDTO> updateProductById(@PathVariable String id,
                                                                @RequestBody ProductRequestDTO productRequestDTO) {
        return ResponseEntity.ok(productService.updateById(id, productRequestDTO));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteByProductId(@PathVariable String id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
