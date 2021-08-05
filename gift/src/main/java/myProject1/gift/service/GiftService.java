package myProject1.gift.service;

import lombok.RequiredArgsConstructor;
import myProject1.gift.dto.GiftItemDto;
import myProject1.gift.domain.*;
import myProject1.gift.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GiftService {
    private final GiftRepository giftRepository;
    private final GiftItemRepository giftItemRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

//    //받는사람 지정만 하는 선물생성
//    public Long createGiftByReceiveName(Long giveMemberId, Long receiveMemberId){
//        //Member 엔티티 조회
//        Member giveMember = memberRepository.findOne(giveMemberId);
//        Member receiveMember = memberRepository.findOne(receiveMemberId);
//
//        // Gift 객체 생성
//        Gift gift = Gift.createGiftByUsername(giveMember, receiveMember);
//
//        // Gift entity 저장
//        Long giftId = giftRepository.save(gift);
//        return giftId;
//    }

    public Long createGift(Long giveMemberId, Long receiveMemberId, String message, GiftItemDto... giftItemDtos){
        //Member 엔티티 조회
        Member giveMember = memberRepository.findOne(giveMemberId);
        Member receiveMember = memberRepository.findOne(receiveMemberId);

        // Item 엔티티 조회 -> GiftItem 엔티티 저장
        List<GiftItem> giftItemList = new ArrayList<>();
        for (GiftItemDto giftItemDTO : giftItemDtos) {
            Item item = itemRepository.findOne(giftItemDTO.getItemId());
            GiftItem giftItem = GiftItem.createGiftItem(item, item.getPrice(), giftItemDTO.getCount());
            giftItemRepository.save(giftItem);
            giftItemList.add(giftItem);
        }

        // Gift 객체 생성
        Gift gift = Gift.createGift(LocalDate.now(), message, giveMember, receiveMember, giftItemList);

        // Gift 엔티티 저장
        Long giftId = giftRepository.save(gift);

        return giftId;
    }

    public void acceptGift(Long giftId){
        Gift gift = giftRepository.findOne(giftId);
        gift.acceptGift();
    }

    public void refuseGift(Long giftId){
        Gift gift = giftRepository.findOne(giftId);
        gift.refuseGift();
    }
}
