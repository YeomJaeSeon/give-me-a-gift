package myProject1.gift.controller;

import lombok.RequiredArgsConstructor;
import myProject1.gift.domain.Category;
import myProject1.gift.domain.Item;
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

        //상품
        Item item1 = Item.createItem("레드와인", 20000, 100, category1);
        Item item2 = Item.createItem("스테이크", 30000, 100, category1);
        Item item3 = Item.createItem("한우 부채살", 50000, 30, category2);
        Item item4 = Item.createItem("색연필", 1000, 10000, category);
        itemService.createItem(item1);
        itemService.createItem(item2);
        itemService.createItem(item3);
        itemService.createItem(item4);

    }

    //==홈 화면 display==//
    @GetMapping("/")
    public String dispHome(Model model){
        List<Category> categories = categoryService.findAllCategories();

        model.addAttribute("categories", categories);
        return "home/index";
    }
}
