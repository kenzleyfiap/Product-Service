package br.com.kenzley.fiap.service.product.infrastructure.repository;

import br.com.kenzley.fiap.service.product.infrastructure.entity.ProductEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<ProductEntity, String> {
}
