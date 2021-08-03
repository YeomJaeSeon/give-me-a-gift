package myProject1.gift.service;

import myProject1.gift.domain.Category;
import myProject1.gift.domain.Item;
import myProject1.gift.dto.ItemDto;
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

        ItemDto itemDto = createItemDto(category.getId(), "itemA", 10000, 20);
        //when
        Long itemId = itemService.createItem(itemDto);
        Item resultItem = em.find(Item.class, itemId);

        //then
        assertThat(itemDto.getName()).isEqualTo(resultItem.getName());
        assertThat(itemDto.getPrice()).isEqualTo(resultItem.getPrice());
        assertThat(itemDto.getStockQuantity()).isEqualTo(resultItem.getStockQuantity());
        assertThat(itemDto.getCategory()).isEqualTo(resultItem.getCategory().getId());
    }

    private ItemDto createItemDto(Long category, String name, int price, int stockQuantity) {
        ItemDto itemDto = new ItemDto();
        itemDto.setName(name);
        itemDto.setPrice(price);
        itemDto.setStockQuantity(stockQuantity);
        itemDto.setCategory(category);

        return itemDto;
    }

    @Test
    void 상품수정(){
        //given
        Category category = createCategory("음식");
        ItemDto itemDto = createItemDto(category.getId(), "itemA", 10000, 20);
        Long updateId = itemService.createItem(itemDto);

        String updateName = "itemAAA";
        int updatePrice = 20000;
        int updateStockQuantity = 200;
        Category updateCategory = createCategory("가구");
        ItemDto itemDto1 = createItemDto(updateId, updateName, updatePrice, updateStockQuantity);

        //when
        itemService.updateItem(updateId, itemDto1);
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
        ItemDto itemDto = createItemDto(category.getId(), "itemA", 10000, 20);
        Long itemId = itemService.createItem(itemDto);

        Item resultItem = em.find(Item.class, itemId);

        //when
        itemService.deleteItem(resultItem);
        List<Item> items = itemService.findItems(null);

        //then
        assertThat(items.size()).isEqualTo(4);
    }

    @Test
    void 카테고리에따른_상품_조회(){
        //given
        Category category = createCategory("음식");
        Category category1 = createCategory("가구");
        Category category2 = createCategory("헬스");

        ItemDto itemDto1 = createItemDto(category.getId(), "itemA", 10000, 20);
        ItemDto itemDto2 = createItemDto(category1.getId(), "itemB", 10000, 20);
        ItemDto itemDto3 = createItemDto(category1.getId(), "itemC", 10000, 20);
        ItemDto itemDto4 = createItemDto(category2.getId(), "itemD", 10000, 20);
        ItemDto itemDto5 = createItemDto(null, "itemE", 10000, 20);

        itemService.createItem(itemDto1);
        itemService.createItem(itemDto2);
        itemService.createItem(itemDto3);
        itemService.createItem(itemDto4);
        itemService.createItem(itemDto5); // d

        //when
        List<Item> items = itemService.findItems(category);
        List<Item> items1 = itemService.findItems(category1);
        List<Item> items2 = itemService.findItems(category2);
        List<Item> items3 = itemService.findItems(null);

        //then
        assertThat(items.size()).isEqualTo(1);

        assertThat(items1.size()).isEqualTo(2);

        assertThat(items2.size()).isEqualTo(1);

        assertThat(items3.size()).isEqualTo(9); //
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