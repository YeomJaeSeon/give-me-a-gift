package myProject1.gift.repository;

import lombok.RequiredArgsConstructor;
import myProject1.gift.domain.Category;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {
    private final EntityManager em;

    public Long save(Category category){
        em.persist(category);
        return category.getId();
    }

    public Long delete(Category category){
        Long deleteId = category.getId();
        em.remove(category);
        return deleteId;
    }

    public List<Category> findAll(){
        List<Category> categories = em.createQuery("select c from Category c", Category.class)
                .getResultList();
        return categories;
    }
}
