package myProject1.gift.controller;

import lombok.RequiredArgsConstructor;
import myProject1.gift.domain.Gift;
import myProject1.gift.domain.GiftItem;
import myProject1.gift.service.GiftService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/gift-info")
public class AdminController {
    private final GiftService giftService;

    //==관리자의 사용자 선물목록 페이지 display==//
    @GetMapping
    public String dispGiftsOfAdmin(Model model){
        List<Gift> gifts = giftService.findAllGifts();
        model.addAttribute("gifts", gifts);

        return "gift/giftList";
    }

    //==관리자의 사용사 선물 상세보기 페이지 display==//
    @GetMapping("/{giftId}/detail")
    public String dispGiftItemsOfAdmin(@PathVariable Long giftId, Model model){
        Gift gift = giftService.findById(giftId);
        List<GiftItem> giftItems = gift.getGiftItems();
        model.addAttribute("giftItems", giftItems);
        model.addAttribute("totalPrice", gift.getTotalPrice());
        return "gift/giftItemList";
    }
}
