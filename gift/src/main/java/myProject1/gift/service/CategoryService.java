package myProject1.gift.service;

import lombok.RequiredArgsConstructor;
import myProject1.gift.domain.Category;
import myProject1.gift.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Long createCategory(Category category){
        Long id = categoryRepository.save(category);
        return id;
    }

    public Long deleteCategory(Category category){
        Long id = categoryRepository.delete(category);
        return id;
    }

    public Category findById(Long id){
        return categoryRepository.findById(id);
    }

    public List<Category> findAllCategories(){
        return categoryRepository.findAll();
    }
}
