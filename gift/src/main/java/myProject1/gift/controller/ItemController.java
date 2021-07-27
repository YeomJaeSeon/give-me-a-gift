package myProject1.gift.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myProject1.gift.domain.Category;
import myProject1.gift.domain.Item;
import myProject1.gift.dto.ItemDto;
import myProject1.gift.service.CategoryService;
import myProject1.gift.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class ItemController {
    private final ItemService itemService;
    private final CategoryService categoryService;

    @PostConstruct
    private void initCategories(){
        Category category = new Category();
        category.setName("음식");
        Category category1 = new Category();
        category1.setName("가전용품");
        Category category2 = new Category();
        category2.setName("건강");
        categoryService.createCategory(category);
        categoryService.createCategory(category1);
        categoryService.createCategory(category2);
    }

    //== admin page (권한 필요)==//
    @GetMapping
    public String adminPage(){

        return "item/admin";
    }

    //==상품 추가 form==//
    @GetMapping("/items/new")
    public String createItemForm(Model model){
        List<Category> categories = categoryService.findAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("itemDto", new ItemDto());
        return "item/createItemForm";
    }

    //==상품추가==//
    @PostMapping("/items/new")
    public String createItem(@ModelAttribute ItemDto itemDto){

        log.info("itemDto 값={}", itemDto);
        Category category = categoryService.findById(itemDto.getCategory());
        Item item = Item.createItem(itemDto.getName(), itemDto.getPrice(), itemDto.getStockQuantity(), category);
        itemService.createItem(item);

        return "redirect:/admin/items";
    }

    //==상품 목록==//
    @GetMapping("/items")
    public String itemList(Model model){
        List<Item> items = itemService.findItems(null);
        model.addAttribute("items", items);
        return "item/itemList";
    }

    //==상품 수정==//
    @GetMapping("/items/{itemId}/edit")
    public String editItem(@PathVariable Long itemId, Model model){
        Item item = itemService.findById(itemId);
        model.addAttribute("item", item);

        return "item/editItemForm";
    }
}
