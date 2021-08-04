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

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class ItemController {
    private final ItemService itemService;
    private final CategoryService categoryService;

    //==관리자 페이지 display==//
    @GetMapping
    public String dispAdminPage(){
        return "item/admin";
    }

    //==상품 생성 페이지 display==//
    @GetMapping("/items/create")
    public String dispItemCreateForm(Model model){
        List<Category> categories = categoryService.findAllCategories();

        model.addAttribute("categories", categories);
        model.addAttribute("item", new ItemDto());

        return "item/itemCreateForm";
    }

    //==상품 생성==//
    @PostMapping("/items/create")
    public String createItem(@Valid @ModelAttribute("item") ItemDto itemDto, BindingResult result, Model model){
        if(result.hasErrors()){
            List<Category> categories = categoryService.findAllCategories();
            model.addAttribute("categories", categories);
            return "item/itemCreateForm";
        }
        log.info("등록 아이템={}", itemDto);
        itemService.createItem(itemDto);

        return "redirect:/admin/items";
    }

    //==상품 목록 display==//
    @GetMapping("/items")
    public String dispItems(Model model){
        List<Item> items = itemService.findAll();
        model.addAttribute("items", items);
        return "item/itemList";
    }

    //==상품 수정 display==//
    @GetMapping("/items/{itemId}/edit")
    public String dispEditItemForm(@PathVariable Long itemId, Model model){
        Item item = itemService.findById(itemId);
        List<Category> categories = categoryService.findAllCategories();

        model.addAttribute("categories", categories);
        model.addAttribute("item", item);
        model.addAttribute("id", item.getId());

        if(item.getCategory() != null){
            log.info("item은 {}", item.getCategory());
        }else{
            log.info("item은 {}", item.getCategory());
        }

        return "item/editItemForm";
    }

    //==상품 수정==//
    @PostMapping("/items/{itemId}/edit")
    public String editItem(@Valid @ModelAttribute("item") ItemDto itemDto, BindingResult result ,@PathVariable Long itemId){
        if(result.hasErrors()){
            return "item/editItemForm";
        }

        itemService.updateItem(itemId, itemDto);
        return "redirect:/admin/items";
    }

    //==상품 삭제==//
    @GetMapping("/items/{itemId}/delete")
    public String deleteItem(@PathVariable Long itemId){
        Item item = itemService.findById(itemId);
        itemService.deleteItem(item);
        return "redirect:/admin/items";
    }
}