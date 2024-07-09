package br.com.kenzley.fiap.service.product.business;

import br.com.kenzley.fiap.service.product.infraescruture.entity.CategoryEntity;
import br.com.kenzley.fiap.service.product.infraescruture.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryEntity saveCategory(CategoryEntity categoryEntity) {
        return categoryRepository.save(categoryEntity);
    }
}
