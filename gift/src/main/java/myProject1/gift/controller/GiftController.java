package myProject1.gift.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myProject1.gift.domain.Category;
import myProject1.gift.domain.Item;
import myProject1.gift.domain.Member;
import myProject1.gift.dto.GiftItemDto;
import myProject1.gift.service.CategoryService;
import myProject1.gift.service.GiftService;
import myProject1.gift.service.ItemService;
import myProject1.gift.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/gift")
public class GiftController {
    private final GiftService giftService;
    private final CategoryService categoryService;
    private final ItemService itemService;
    private final MemberService memberService;

    //==선물페이지 조회==//
    @GetMapping
    public String giftPage(Model model){
        List<Category> categories = categoryService.findAllCategories();
        model.addAttribute("categories", categories);
        return "gift/home";
    }

    //==카테고리별 상품 조회==//
    @GetMapping("/category/{categoryId}")
    public String categoryItemsList(Model model, @PathVariable Long categoryId){
        Category category = categoryService.findById(categoryId);
        List<Item> items = itemService.findItems(category);
        log.info("카테고리별 아이템들: {}", items);
        model.addAttribute("name", category.getName());
        model.addAttribute("items", items);
        return "gift/categoryItemList";
    }

    //==선물 상품 조회페이지==//
    @GetMapping("/items/{itemId}")
    public String createGiftItemForm(@PathVariable Long itemId, Model model){
        List<Member> members = memberService.findAllMembers();
        Item item = itemService.findById(itemId);
        model.addAttribute("members", members);
        model.addAttribute("item", item);
        return "gift/giftItem";
    }

//    //==선물 상품 장바구니에 담기==//
//    @PostMapping("/items/{itemId}")
//    public String createGiftItem(@PathVariable Long itemId,
//                                 @RequestParam Long send,
//                                 @RequestParam String name,
//                                 @RequestParam int price,
//                                 @RequestParam int count
//                                 ){
//
//    }
}
