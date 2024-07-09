package br.com.kenzley.fiap.service.product.api.response;

import br.com.kenzley.fiap.service.product.api.request.CategoryRequestDTO;

public record ProductResponseDTO(
                                     String id,
                                     String name,
                                     Double price,
                                     String information,
                                     CategoryRequestDTO category) {
}
