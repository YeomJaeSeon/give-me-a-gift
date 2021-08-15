package myProject1.gift.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myProject1.gift.domain.*;
import myProject1.gift.dto.GiftItemDto;
import myProject1.gift.service.CategoryService;
import myProject1.gift.service.GiftService;
import myProject1.gift.service.ItemService;
import myProject1.gift.service.MemberService;
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
@RequestMapping("/gift")
public class GiftController {
    private final GiftService giftService;
    private final ItemService itemService;
    private final CategoryService categoryService;
    private final MemberService memberService;

    //==선물 받을 상대 선택==//
    @GetMapping("/members/{receiveMemberId}")
    public String selectReceiveMember(@PathVariable Long receiveMemberId, HttpServletRequest request){

        HttpSession session = request.getSession();
        session.setAttribute("receiveMemberId", receiveMemberId);
        //receiveMemberId 세션에 선물 받는 사람의 아이디를저장

        return "redirect:/";
    }

    //==선물을 위한 카테고리의 상품 display 페이지 조회==//
    @GetMapping("/categories/{categoryId}")
    public String dispCategoryItems(@PathVariable Long categoryId, Model model){
        List<Item> items = null;
        if(categoryId != 0){
            //특정 카테고리의 아이템 조회
            Category category = categoryService.findById(categoryId);
            items = itemService.findByItems(category);
            model.addAttribute("category", category.getName());
        }else{
            //기타 카테고리의 아이템 조회
            items = itemService.findByItems(null);
            model.addAttribute("category", "기타");
        }

        model.addAttribute("items", items);

        return "gift/itemsByCategoryForGift";
    }

    //==선물할 상품 상세보기 페이지 display==//
    @GetMapping("/items/{itemId}")
    public String dispGiftSpecificItem(@PathVariable Long itemId, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes){
        HttpSession session = request.getSession();
        Long receiveMemberId = (Long) session.getAttribute("receiveMemberId");
        if(receiveMemberId == null){
            //선물 받는이 지정 안했으면
            //선물 받는이 지정하는곳으로 redirect
            redirectAttributes.addFlashAttribute("message", "선물할 회원을 먼저 선택해주세요");
            return "redirect:/members";
        }
        Item item = itemService.findById(itemId);
        model.addAttribute("item", item);
        model.addAttribute("giftItemDto", new GiftItemDto());
        return "gift/item";
    }

    //==상품 단건 선물하기==//
    @PostMapping("/items/{itemId}")
    public String giveOneGift(@PathVariable Long itemId, HttpServletRequest request ,@Valid @ModelAttribute GiftItemDto giftItemDto, BindingResult result, Model model, @RequestParam String message){
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
        HttpSession session = request.getSession();
        Long receiveMemberId = (Long) session.getAttribute("receiveMemberId");

        List<Member> members = getLoginedMember();
        Member giveMember = members.get(0);
        giftItemDto.setItemId(itemId);

        if(message.trim() == ""){
            message = "제 마음입니다 ^^";
        }

        log.info("giftItemDto price : {}", giftItemDto.getPrice());

        giftService.createOneGift(giveMember.getId(), receiveMemberId, message, giftItemDto);

        return "redirect:/";
    }


    //==선물 보관함 페이지 display==//
    @GetMapping("/gift-box")
    public String dispGiftBox(Model model){
        List<Member> members = getLoginedMember();
        Member member = members.get(0);
        List<Gift> gifts = giftService.receiveGiftsOfMember(member);

        model.addAttribute("gifts", gifts);

        return "gift/receiveGiftList";
    }

    //==선물보관함에서 선물 정보 보기 페이지 display==//
    @GetMapping("/gift-box/{giftId}")
    public String dispGiftInfo(@PathVariable Long giftId, Model model){
        Gift gift = giftService.findById(giftId);
        List<GiftItem> giftItems = gift.getGiftItems();

        model.addAttribute("giftItems", giftItems);

        return "gift/giftInfo";
    }

    //==선물 보관함에서 선물 수락
    @GetMapping("/gift-box/accept/{giftId}")
    public String acceptGift(@PathVariable Long giftId){
        giftService.acceptGift(giftId);

        return "redirect:/gift/gift-box";
    }

    //==선물 보관함에서 선물 거부
    @GetMapping("/gift-box/refuse/{giftId}")
    public String refuseGift(@PathVariable Long giftId){
        giftService.refuseGift(giftId);

        return "redirect:/gift/gift-box";
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
