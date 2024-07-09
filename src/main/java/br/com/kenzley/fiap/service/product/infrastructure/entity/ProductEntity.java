package br.com.kenzley.fiap.service.product.infrastructure.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "product_entity")
public class ProductEntity {

    @Id
    private String id;
    private String name;
    private Double price;
    private String information;
}
