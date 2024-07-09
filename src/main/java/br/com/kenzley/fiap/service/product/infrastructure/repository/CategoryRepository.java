package br.com.kenzley.fiap.service.product.infrastructure.repository;

import br.com.kenzley.fiap.service.product.infrastructure.entity.CategoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<CategoryEntity, String> {
    CategoryEntity findByProductId(String productId);

    void deleteByProductId(String productId);
}
