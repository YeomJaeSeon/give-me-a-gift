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
        Long deleteId = giftItemRepository.delete(giftItem);
        return deleteId;
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
