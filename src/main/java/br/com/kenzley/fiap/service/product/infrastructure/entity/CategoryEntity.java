package br.com.kenzley.fiap.service.product.infrastructure.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "category_entity")
public class CategoryEntity {

    private String name;
    private String productId;
}
