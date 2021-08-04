package myProject1.gift.service;

import myProject1.gift.domain.Category;
import myProject1.gift.dto.CategoryDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CategoryServiceTest {
    @Autowired
    CategoryService categoryService;
    @Autowired
    EntityManager em;

    @Test
    void 카테고리생성(){
        //given
        CategoryDto category = new CategoryDto();
        category.setCategory("음식");

        //when
        Long categoryId = categoryService.createCategory(category);
        Category resultCategory = categoryService.findById(categoryId);

        //then
        assertThat(category.getCategory()).isEqualTo(resultCategory.getName());
    }

    @Test
    void 카테고리_모두_조회(){
        //given
        CategoryDto category = new CategoryDto();
        category.setCategory("음식");
        CategoryDto category1 = new CategoryDto();
        category1.setCategory("헬스");
        CategoryDto category2 = new CategoryDto();
        category2.setCategory("가구");

        Long category1Id = categoryService.createCategory(category);
        Long category2Id = categoryService.createCategory(category1);
        Long category3Id = categoryService.createCategory(category2);

        //when
        List<Category> categories = categoryService.findAllCategories();

        //then
        assertThat(categories.size()).isEqualTo(6);
    }

    @Test
    void 카테고리_삭제(){
        //given
        CategoryDto category = new CategoryDto();
        category.setCategory("음료");

        Long categoryId = categoryService.createCategory(category);
        Category findCategory = categoryService.findById(categoryId);

        //when
        categoryService.deleteCategory(findCategory);
        List<Category> categories = categoryService.findAllCategories();

        //then
        assertThat(categories.size()).isEqualTo(3);
    }
}