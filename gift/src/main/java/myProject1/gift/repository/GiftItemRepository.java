package myProject1.gift.repository;

import lombok.RequiredArgsConstructor;
import myProject1.gift.domain.Basket;
import myProject1.gift.domain.GiftItem;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GiftItemRepository {
    private final EntityManager em;

    public Long save(GiftItem giftItem){
        em.persist(giftItem);
        return giftItem.getId();
    }

    public Long delete(GiftItem giftItem){
        Long deleteId = giftItem.getId();
        em.remove(giftItem);
        return deleteId;
    }

    //선물바구니로 선물 상품찾기
    public List<GiftItem> findByBasket(Basket basket){
        List<GiftItem> giftItems = em.createQuery("select gi from GiftItem gi where gi.basket = :basket", GiftItem.class)
                .setParameter("basket", basket)
                .getResultList();
        return giftItems;
    }

    public void updateBasketToNull(GiftItem giftItem){
        giftItem.setBasket(null);
    }
}
