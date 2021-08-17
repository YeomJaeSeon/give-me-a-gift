package myProject1.gift.service;

import lombok.RequiredArgsConstructor;
import myProject1.gift.domain.Gift;
import myProject1.gift.domain.GiftItem;
import myProject1.gift.domain.Item;
import myProject1.gift.domain.Member;
import myProject1.gift.dto.GiftItemDto;
import myProject1.gift.repository.GiftItemRepository;
import myProject1.gift.repository.GiftRepository;
import myProject1.gift.repository.ItemRepository;
import myProject1.gift.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GiftService {
    private final GiftRepository giftRepository;
    private final GiftItemRepository giftItemRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    //선물 생성
    //GiftItem 생성 후 Gift도 바로 생성
    public Long createOneGift(Long giveMemberId, Long receiveMemberId, String message, GiftItemDto giftItemDto){
        //Member 엔티티 조회
        Member giveMember = memberRepository.findOne(giveMemberId);
        Member receiveMember = memberRepository.findOne(receiveMemberId);

        // List GiftItems에 giftItem담기


        Item item = itemRepository.findOne(giftItemDto.getItemId()); //상품 엔티티 조회
        GiftItem giftItem = GiftItem.createGiftItem(item, giftItemDto.getPrice(), giftItemDto.getCount());//giftItem 생성
        giftItemRepository.save(giftItem);

        //선물할 상품의 재고줄이기
        giftItem.createGiftItem();

        // List to Array
        GiftItem[] giftItemsArr = new GiftItem[1];
        giftItemsArr[0] = giftItem;

        // Gift 객체 생성
        Gift gift = Gift.createGift(LocalDate.now(), message, giveMember, receiveMember, giftItemsArr);

        // Gift 엔티티 저장
        Long giftId = giftRepository.save(gift);

        return giftId;
    }

    //선물바구니의 선물들 한번에 선물하기(여러 선물 한번에 선물)
    public Long createGiftFromBasket(Long giveMemberId, Long receiveMemberId, String message, List<GiftItem> giftItems){
        //Member 엔티티 조회
        Member giveMember = memberRepository.findOne(giveMemberId);
        Member receiveMember = memberRepository.findOne(receiveMemberId);

        // List to Array
        GiftItem[] giftItemsArr = new GiftItem[giftItems.size()];
        for(int i = 0; i < giftItemsArr.length; i++){
            giftItemsArr[i] = giftItems.get(i);
        }

        // Gift 객체 생성
        Gift gift = Gift.createGift(LocalDate.now(), message, giveMember, receiveMember, giftItemsArr);

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

    @Transactional(readOnly = true)
    public List<Gift> receiveGiftsOfMember(Member member){
        return giftRepository.findGiftByReceiveMember(member);
    }

    @Transactional(readOnly = true)
    public int findCreatedGifts(Member member){
        int count = giftRepository.findCreatedGifts(member);
        return count;
    }
    @Transactional(readOnly = true)
    public Gift findById(Long id){
        Gift gift = giftRepository.findOne(id);
        return gift;
    }

    @Transactional(readOnly = true)
    public List<Gift> findAllGifts(){
        return giftRepository.findGifts();
    }
}
