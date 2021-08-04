package myProject1.gift.controller;

import lombok.RequiredArgsConstructor;
import myProject1.gift.domain.Category;
import myProject1.gift.domain.Item;
import myProject1.gift.dto.ItemDto;
import myProject1.gift.service.CategoryService;
import myProject1.gift.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final CategoryService categoryService;
    private final ItemService itemService;

    //==상품과 카테고리 테스트 값==//
    @PostConstruct
    public void initCategories(){
        //카테고리
        Category category = new Category();
        category.setName("간단한 선물");
        Category category1 = new Category();
        category1.setName("고급진 선물");
        Category category2 = new Category();
        category2.setName("맛있는 선물");

        categoryService.createCategory(category);
        categoryService.createCategory(category1);
        categoryService.createCategory(category2);

        ItemDto itemDto1 = createItemDto("레드와인", 20000, 100, category1.getId());
        ItemDto itemDto2 = createItemDto("스테이크", 30000, 100, category1.getId());
        ItemDto itemDto3 = createItemDto("한우 부채살", 50000, 30, category2.getId());
        ItemDto itemDto4 = createItemDto("색연필", 1000, 10000, category.getId());

        itemService.createItem(itemDto1);
        itemService.createItem(itemDto2);
        itemService.createItem(itemDto3);
        itemService.createItem(itemDto4);

    }

    private ItemDto createItemDto(String name, int price, int stockQuantity, Long category) {
        ItemDto itemDto = new ItemDto();
        itemDto.setName(name);
        itemDto.setPrice(price);
        itemDto.setStockQuantity(stockQuantity);
        itemDto.setCategory(category);

        return itemDto;
    }

    //==홈 화면 display==//
    @GetMapping("/")
    public String dispHome(Model model){
        List<Category> categories = categoryService.findAllCategories();
        List<Item> items = itemService.findByItems(null);
        // 기타 카테고리의 아이템 조회

        model.addAttribute("categories", categories);
        model.addAttribute("etcCount", items.size());
        return "home/index";
    }
}
