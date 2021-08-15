package myProject1.gift.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myProject1.gift.domain.Basket;
import myProject1.gift.domain.GiftItem;
import myProject1.gift.domain.Item;
import myProject1.gift.domain.Member;
import myProject1.gift.dto.GiftItemDto;
import myProject1.gift.repository.BasketRepository;
import myProject1.gift.repository.GiftItemRepository;
import myProject1.gift.repository.MemberRepository;
import myProject1.gift.service.GiftService;
import myProject1.gift.service.ItemService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/basket")
public class BasketController {
    private final BasketRepository basketRepository;
    private final ItemService itemService;
    private final GiftItemRepository giftItemRepository;
    private final MemberRepository memberRepository;
    private final GiftService giftService;

    //==선물바구니에 선물담기
    @PostMapping("/{itemId}/add")
    public String addGiftItemInBasket(@PathVariable Long itemId, @Valid @ModelAttribute GiftItemDto giftItemDto, BindingResult result, Model model){
        if(result.hasErrors()){
            Item item = itemService.findById(itemId);
            model.addAttribute("item", item);
            return "gift/item";
        }
        Item item = itemService.findById(itemId);
        //상품의 재고가 더 부족하면 에러 메시지 뷰로 전달
        if(item.getStockQuantity() < giftItemDto.getCount()){
            model.addAttribute("item", item);
            model.addAttribute("stockException", true);

            return "gift/item";
        }
        List<Member> members = getLoginedMember();
        Member giveMember = members.get(0);
        giftItemDto.setItemId(itemId);

        GiftItem giftItem = GiftItem.createGiftItem(item, giftItemDto.getPrice(), giftItemDto.getCount());
        giftItemRepository.save(giftItem); //선물 상품 저장

        Basket basket = basketRepository.findByMember(giveMember);
        //바구니 - 선물상품 연관관계 설정
        basket.getGiftItems().add(giftItem);
        giftItem.setBasket(basket);

        basketRepository.save(basket); //선물바구니 저장

        return "redirect:/";
    }

    //==선물바구니 페이지 display==//
    @GetMapping
    public String dispBasket(Model model){
        List<Member> members = getLoginedMember();
        Member loginMember = members.get(0);

        Basket basket = basketRepository.findByMember(loginMember);

        List<GiftItem> giftItems = giftItemRepository.findByBasket(basket);
        model.addAttribute("giftItems", giftItems);

        return "basket/basket";
    }

    //==선물바구니 선물하기==//
    @PostMapping
    public String createGift(HttpServletRequest request, @RequestParam String message){
        HttpSession session = request.getSession();
        Long receiveMemberId = (Long) session.getAttribute("receiveMemberId");

        List<Member> members = getLoginedMember();
        Member loginMember = members.get(0);

        Basket basket = basketRepository.findByMember(loginMember);

        List<GiftItem> giftItems = giftItemRepository.findByBasket(basket);

        giftService.createGiftFromBasket(loginMember.getId(), receiveMemberId, message, giftItems);

        return "redirect:/";
    }

    //============ sub methods (not controller) =================//

    //==현재 로그인한 회원정보 가져오는 메서드==//
    private List<Member> getLoginedMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<Member> members = memberRepository.findByUsername(username);
        log.info("로그인한 회원들 : {}", members);
        return members;
    }
}
