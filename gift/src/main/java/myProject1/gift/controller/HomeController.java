package myProject1.gift.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myProject1.gift.domain.*;
import myProject1.gift.dto.CategoryDto;
import myProject1.gift.dto.ItemDto;
import myProject1.gift.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    private final CategoryService categoryService;
    private final ItemService itemService;
    private final MemberService memberService;
    private final GiftService giftService;
    private final GiftItemService giftItemService;
    private final BasketService basketService;

    //==상품과 카테고리 테스트 값==//
    @PostConstruct
    public String initCategories(){
        //카테고리
        CategoryDto category = new CategoryDto();
        category.setCategory("간단한 선물");
        CategoryDto category1 = new CategoryDto();
        category1.setCategory("고급진 선물");
        CategoryDto category2 = new CategoryDto();
        category2.setCategory("맛있는 선물");

        Long category1Id = categoryService.createCategory(category);
        Long category2Id = categoryService.createCategory(category1);
        Long category3Id = categoryService.createCategory(category2);

        ItemDto itemDto1 = createItemDto("레드와인", 20000, 100, category2Id);
        ItemDto itemDto2 = createItemDto("스테이크", 30000, 100, category2Id);
        ItemDto itemDto3 = createItemDto("한우 부채살", 50000, 30, category3Id);
        ItemDto itemDto4 = createItemDto("색연필", 1000, 10000, category1Id);

        itemService.createItem(itemDto1);
        itemService.createItem(itemDto2);
        itemService.createItem(itemDto3);
        itemService.createItem(itemDto4);

        return "redirect:/user/logout";
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
    public String dispHome(Model model, HttpServletRequest request, Principal principal){
        log.info("principal : {}", principal);
        List<Category> categories = categoryService.findAllCategories();
        List<Item> items = itemService.findByItems(null);
        // 기타 카테고리의 아이템 조회

        HttpSession session = request.getSession();
        Long receiveMemberId = (Long) session.getAttribute("receiveMemberId");
        //세션으로부터 선물 받을 대상 회원의 id 가져옴

        if(receiveMemberId != null){
            //선물받을 대상 지정했으면
            model.addAttribute("isExistReceiveMember", true);
            if(principal != null){
                //로그인 했으면
                List<Member> members = memberService.findByUsername(principal.getName());
                if(members.size() > 0 && receiveMemberId == members.get(0).getId()){
                    //자기 자신에게 선물이면
                    model.addAttribute("target", "나에게 선물하세요!");
                }else if(members.size() > 0 && receiveMemberId != members.get(0).getId()){
                    //다른사람에게 선물이면
                    Member receiveMember = memberService.findById(receiveMemberId);
                    model.addAttribute("target", receiveMember.getUsername() + "에게 선물하세요!");
                }
            }
        }

        //로그인 되어있으면
        if(principal != null) {
            //확인하지않은 선물이 있는지 표시
            List<Member> members = memberService.findByUsername(principal.getName());

            int giftCount = 0;
            if(members.size() > 0) giftCount = giftService.findCreatedGifts(members.get(0));
            if(giftCount != 0){
                //수락이나 거절하지않은 즉, 확인하지않은 선물이 존재하면
                model.addAttribute("isExistNotCheckGifts", true);
            }

            //선물바구니 안에 선물하지않은 선물상품이 있는지 표시
            Basket basket = null;
            if(members.size() > 0) basket = basketService.findBasketById(members.get(0).getBasket().getId());
            List<GiftItem> giftItemsByBasket = giftItemService.findGiftItemsByBasket(basket);
            if(giftItemsByBasket.size() > 0){
                //선물바구니안에 선물하지않은 선물상품이 있으면
                model.addAttribute("isExistNotCheckGiftItemsInBasket", true);
            }
        }

        model.addAttribute("categories", categories);
        model.addAttribute("etcCount", items.size());
        return "home/index";
    }
}
