package br.com.kenzley.fiap.service.product.infraescruture.repository;

import br.com.kenzley.fiap.service.product.infraescruture.entity.ProductEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<ProductEntity, String> {
}
