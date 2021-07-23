package myProject1.gift.service;

import myProject1.gift.domain.Category;
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
        Category category = new Category();
        category.setName("음식");

        //when
        categoryService.createCategory(category);
        Category resultCategory = em.find(Category.class, category.getId());

        //then
        assertThat(category).isEqualTo(resultCategory);
    }

    @Test
    void 카테고리_모두_조회(){
        //given
        Category category = new Category();
        category.setName("음식");
        Category category1 = new Category();
        category.setName("헬스");
        Category category2 = new Category();
        category2.setName("가구");

        categoryService.createCategory(category);
        categoryService.createCategory(category1);
        categoryService.createCategory(category2);

        //when
        List<Category> categories = categoryService.findAllCategories();

        //then
        assertThat(categories.size()).isEqualTo(3);
        assertThat(categories).contains(category, category1 ,category2);
    }

    @Test
    void 카테고리_삭제(){
        //given
        Category category = new Category();
        category.setName("음료");

        categoryService.createCategory(category);
        Long categoryId = category.getId();

        //when
        categoryService.deleteCategory(category);
        Category findCategory = em.find(Category.class, categoryId);
        List<Category> categories = categoryService.findAllCategories();

        //then
        assertThat(findCategory).isEqualTo(null);
        assertThat(categories.size()).isEqualTo(0);
    }
}