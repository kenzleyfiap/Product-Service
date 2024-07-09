package br.com.kenzley.fiap.service.product.infraescruture.repository;

import br.com.kenzley.fiap.service.product.infraescruture.entity.CategoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<CategoryEntity, String> {
}
