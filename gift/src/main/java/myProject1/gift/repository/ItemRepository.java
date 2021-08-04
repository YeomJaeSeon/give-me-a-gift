package myProject1.gift.repository;

import lombok.RequiredArgsConstructor;
import myProject1.gift.domain.Category;
import myProject1.gift.domain.Item;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    public Long save(Item item){
        em.persist(item);
        return item.getId();
    }

    public void update(Long itemId, String name, int price, int stockQuantity, Category category){
        Item item = em.find(Item.class, itemId);
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
        item.setCategory(category);
    }

    public Long delete(Item item){
        Long deleteId = item.getId();
        em.remove(item);
        return deleteId;
    }

    public Item findOne(Long itemId){
        Item resultItem = em.find(Item.class, itemId);
        return resultItem;
    }

    public List<Item> findByCategory(Category category){
        if(category == null){
            // 기타 카테고리의 아이템 조회 (카테고리 이름지정안하면 기타 카테고리 조회)
            List<Item> items = em.createQuery("select i from Item i where i.category IS NULL", Item.class)
                    .getResultList();
            return items;
        }else{
            // 입력된 카테고리의 아이템 조회
            List<Item> items = em.createQuery("select i from Item i where i.category = :category", Item.class)
                    .setParameter("category", category)
                    .getResultList();
            return items;
        }
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }

}
