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
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    public Long createGift(Long giveMemberId, Long receiveMemberId, Long messageId, GiftItemDto... giftItemDtos){
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

        // Message 엔티티 조회
        Message message = messageRepository.findOne(messageId);

        // Gift 엔티티 생성
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
