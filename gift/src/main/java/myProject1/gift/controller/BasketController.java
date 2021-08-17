package myProject1.gift.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myProject1.gift.domain.Basket;
import myProject1.gift.domain.GiftItem;
import myProject1.gift.domain.Item;
import myProject1.gift.domain.Member;
import myProject1.gift.dto.GiftItemDto;
import myProject1.gift.service.*;
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
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/basket")
public class BasketController {
    private final ItemService itemService;
    private final GiftService giftService;
    private final MemberService memberService;
    private final GiftItemService giftItemService;
    private final BasketService basketService;

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
        giftItemService.createGiftItem(giftItem); //선물 상품 저장

        Basket basket = basketService.findBasketByMember(giveMember);
        //바구니 - 선물상품 연관관계 설정
        basket.addGiftItem(giftItem);

        basketService.createBasket(basket); //선물바구니 저장

        return "redirect:/";
    }

    //==선물바구니 페이지 display==//
    @GetMapping
    public String dispBasket(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        Long receiveMemberId = (Long)session.getAttribute("receiveMemberId");

        if(receiveMemberId != null){
            //선물받을 대상 지정했으면
            model.addAttribute("isExistReceiveMember", true);
            List<Member> members = getLoginedMember();
            if(receiveMemberId == members.get(0).getId()){
                //자기 자신에게 선물이면
                model.addAttribute("target", "나에게 선물하세요!");
            }else{
                //다른사람에게 선물이면
                Member receiveMember = memberService.findById(receiveMemberId);
                model.addAttribute("target", receiveMember.getUsername() + "에게 선물하세요!");
            }
        }

        List<Member> members = getLoginedMember();
        Member loginMember = members.get(0);

        Basket basket = basketService.findBasketByMember(loginMember);

        List<GiftItem> giftItems = giftItemService.findGiftItemsByBasket(basket);
        model.addAttribute("giftItems", giftItems);

        return "basket/basket";
    }

    //==선물바구니 선물하기==//
    @PostMapping
    public String createGift(HttpServletRequest request, @RequestParam String message, RedirectAttributes redirectAttributes){
        HttpSession session = request.getSession();
        Long receiveMemberId = (Long) session.getAttribute("receiveMemberId");
        if(receiveMemberId == null){
            redirectAttributes.addFlashAttribute("message", "선물할 회원을 먼저 선택해주세요");
            return "redirect:/members";
        }

        //현재 로그인한 유저 정보 가져오기
        List<Member> members = getLoginedMember();
        Member loginMember = members.get(0);

        //로그인한 멤버의 basket
        Basket basket = basketService.findBasketByMember(loginMember);

        //해당 basket의 giftItem들 가져오기
        List<GiftItem> giftItems = giftItemService.findGiftItemsByBasket(basket);
        //바구니에 선물상품 아무것도없으면 선물할수없음
        if(giftItems.size() == 0){
            redirectAttributes.addFlashAttribute("message", "선물바구니가 비어서 선물할수 없습니다.");
            return "redirect:/basket";
        }

        //
        giftService.createGiftFromBasket(loginMember.getId(), receiveMemberId, message, giftItems);
        for (GiftItem giftItem : giftItems) {
            giftItemService.updateGiftItemBasketToNull(giftItem);
        }

        return "redirect:/";
    }

    //============ sub methods (not controller) =================//

    //==현재 로그인한 회원정보 가져오는 메서드==//
    private List<Member> getLoginedMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<Member> members = memberService.findByUsername(username);
        log.info("로그인한 회원들 : {}", members);
        return members;
    }
}
