package myProject1.gift.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myProject1.gift.domain.Category;
import myProject1.gift.domain.Item;
import myProject1.gift.dto.CategoryDto;
import myProject1.gift.dto.ItemDto;
import myProject1.gift.service.CategoryService;
import myProject1.gift.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
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

        Item item1 = Item.createItem("햄버거", 20000, 100, category);
        Item item2 = Item.createItem("피자", 25000, 150, category);
        Item item3 = Item.createItem("아메리카노", 2000, 50, category);
        Item item4 = Item.createItem("바벨", 2000000, 10, category2);
        Item item5 = Item.createItem("덤벨", 100000, 20, category2);
        Item item6 = Item.createItem("컴퓨터", 30000, 1000, category1);
        itemService.createItem(item1);
        itemService.createItem(item2);
        itemService.createItem(item3);
        itemService.createItem(item4);
        itemService.createItem(item5);
        itemService.createItem(item6);
    }

    //== admin page (권한 필요) 슾흐링 시큐리티로 인가를 적용해야함..==//
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
    public String createItem(@Valid @ModelAttribute ItemDto itemDto, BindingResult result, Model model){
        if(result.hasErrors()){
            List<Category> categories = categoryService.findAllCategories();
            model.addAttribute("categories", categories);
            return "item/createItemForm";
        }

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

    //==상품 수정 page==//
    @GetMapping("/items/{itemId}/edit")
    public String editItemForm(@PathVariable Long itemId, Model model){
        List<Category> categories = categoryService.findAllCategories();
        model.addAttribute("categories", categories);
        Item item = itemService.findById(itemId);

        ItemDto itemDto = new ItemDto();
        itemDto.setName(item.getName());
        itemDto.setCategory(item.getCategory().getId());
        itemDto.setPrice(item.getPrice());
        itemDto.setStockQuantity(item.getStockQuantity());

        model.addAttribute("itemDto", itemDto);

        return "item/editItemForm";
    }

    //==상품 수정==//
    @PostMapping("/items/{itemId}/edit")
    public String editItem(@Valid @ModelAttribute ItemDto itemDto, BindingResult result ,@PathVariable Long itemId, Model model){
        if(result.hasErrors()){
            List<Category> categories = categoryService.findAllCategories();
            model.addAttribute("categories", categories);

            return "item/editItemForm";
        }
        Category category = categoryService.findById(itemDto.getCategory());

        itemService.updateItem(itemId, itemDto.getName(), itemDto.getPrice(), itemDto.getStockQuantity(), category);
        return "redirect:/admin/items";
    }

    //==상품 삭제==//
    @GetMapping("/items/{itemId}/delete")
    public String deleteItem(@PathVariable Long itemId){
        Item item = itemService.findById(itemId);
        itemService.deleteItem(item);

        return "redirect:/admin/items";
    }

    //==카테고리 추가 페이지 조회==//
    @GetMapping("category/new")
    public String createCategoryForm(Model model){
        model.addAttribute("categoryDto", new CategoryDto());
        return "item/createCategoryForm";
    }

    //==카테고리 추가==//
    @PostMapping("/category/new")
    public String createCategory(@Valid @ModelAttribute CategoryDto categoryDto, BindingResult result){
        log.info("카테고리 이름 : {}", categoryDto.getCategory());
        if(result.hasErrors()){
            return "item/createCategoryForm";
        }
        Category newCategory = new Category();
        newCategory.setName(categoryDto.getCategory());
        categoryService.createCategory(newCategory);

        return "redirect:/admin/items/new";
    }
}