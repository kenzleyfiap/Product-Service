package br.com.kenzley.fiap.service.product.api;

import br.com.kenzley.fiap.service.product.api.request.ProductRequestDTO;
import br.com.kenzley.fiap.service.product.api.response.ProductResponseDTO;
import br.com.kenzley.fiap.service.product.business.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping()
    public ResponseEntity<ProductResponseDTO> engraveProducts(@RequestBody ProductRequestDTO productRequestDTO) {
        return ResponseEntity.ok(productService.engraveProducts(productRequestDTO));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> findAllProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable String id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PutMapping("/{id}")
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
