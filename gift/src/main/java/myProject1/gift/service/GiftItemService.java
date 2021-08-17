package myProject1.gift.service;

import lombok.RequiredArgsConstructor;
import myProject1.gift.domain.Basket;
import myProject1.gift.domain.GiftItem;
import myProject1.gift.repository.GiftItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GiftItemService {
    private final GiftItemRepository giftItemRepository;

    public Long createGiftItem(GiftItem giftItem){
        Long saveId = giftItemRepository.save(giftItem);
        return saveId;
    }

    public Long deleteGiftItem(GiftItem giftItem){
        giftItem.refused(); // 선물상품은 오직 선물바구니에서만 삭제됨. 그렇기에 선물상품이 삭제되면 상품의 재고를 다시 늘려줘야함.
        //상품의 재고를 원복하는 문장.
        Long deleteId = giftItemRepository.delete(giftItem);
        return deleteId;
    }

    @Transactional(readOnly = true)
    public GiftItem findOneGiftItemById(Long giftItemId){
        return giftItemRepository.findById(giftItemId);
    }

    @Transactional(readOnly = true)
    public List<GiftItem> findGiftItemsByBasket(Basket basket){
        List<GiftItem> giftItems = giftItemRepository.findByBasket(basket);
        return giftItems;
    }

    public void updateGiftItemBasketToNull(GiftItem giftItem){
        giftItemRepository.updateBasketToNull(giftItem);
    }
}
