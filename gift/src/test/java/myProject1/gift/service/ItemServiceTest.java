package myProject1.gift.service;

import myProject1.gift.domain.Category;
import myProject1.gift.domain.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    ItemService itemService;
    @Autowired
    EntityManager em;

    @Test
    void 상품생성(){
        //given
        Category category = createCategory("음식");

        Item item = createItem(category, "itemA", 10000, 20);

        //when
        itemService.createItem(item);
        Item resultItem = em.find(Item.class, item.getId());

        //then
        assertThat(item).isEqualTo(resultItem);
    }

    @Test
    void 상품수정(){
        //given
        Category category = createCategory("음식");
        Item item = createItem(category, "itemA", 10000, 20);
        itemService.createItem(item);

        Long updateId = item.getId();
        String updateName = "itemAAA";
        int updatePrice = 20000;
        int updateStockQuantity = 200;
        Category updateCategory = createCategory("가구");

        //when
        itemService.updateItem(updateId, updateName, updatePrice, updateStockQuantity, updateCategory);
        Item resultItem = em.find(Item.class, updateId);

        //then
        assertThat(resultItem.getName()).isEqualTo(updateName);
        assertThat(resultItem.getPrice()).isEqualTo(updatePrice);
        assertThat(resultItem.getStockQuantity()).isEqualTo(updateStockQuantity);
        assertThat(resultItem.getCategory()).isEqualTo(updateCategory);
    }

    @Test
    void 상품삭제(){
        //given
        Category category = createCategory("음식");
        Item item = createItem(category, "itemA", 10000, 20);
        itemService.createItem(item);

        Item resultItem = em.find(Item.class, item.getId());

        //when
        itemService.deleteItem(resultItem);
        List<Item> items = itemService.findItems(null);

        //then
        assertThat(items.size()).isEqualTo(0);
    }

    @Test
    void 카테고리에따른_상품_조회(){
        //given
        Category category = createCategory("음식");
        Category category1 = createCategory("가구");
        Category category2 = createCategory("헬스");

        Item item1 = createItem(category, "itemA", 10000, 20);
        Item item2 = createItem(category1, "itemB", 10000, 20);
        Item item3 = createItem(category1, "itemC", 10000, 20);
        Item item4 = createItem(category2, "itemD", 10000, 20);
        Item item5 = createItem(null, "itemE", 10000, 20);
        itemService.createItem(item1);
        itemService.createItem(item2);
        itemService.createItem(item3);
        itemService.createItem(item4);
        itemService.createItem(item5);

        //when
        List<Item> items = itemService.findItems(category);
        List<Item> items1 = itemService.findItems(category1);
        List<Item> items2 = itemService.findItems(category2);
        List<Item> items3 = itemService.findItems(null);

        //then
        assertThat(items.size()).isEqualTo(1);
        assertThat(items).contains(item1);

        assertThat(items1.size()).isEqualTo(2);
        assertThat(items1).contains(item2, item3);

        assertThat(items2.size()).isEqualTo(1);
        assertThat(items2).contains(item4);

        assertThat(items3.size()).isEqualTo(5);
        assertThat(items3).contains(item1, item2, item3, item4, item5);
    }

    private Item createItem(Category category, String name, int price, int stockQuantity) {
        Item item = Item.createItem(name, price, stockQuantity, category);

        return item;
    }


    private Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        em.persist(category);

        return category;
    }
}