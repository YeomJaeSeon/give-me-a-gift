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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String createItem(@ModelAttribute ItemDto itemDto){
        itemService.createItem(itemDto);

        return "redirect:/admin/items";
    }
}
