package myProject1.gift.repository;

import lombok.RequiredArgsConstructor;
import myProject1.gift.domain.GiftItem;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class GiftItemRepository {
    private final EntityManager em;

    public Long save(GiftItem giftItem){
        em.persist(giftItem);
        return giftItem.getId();
    }
}
