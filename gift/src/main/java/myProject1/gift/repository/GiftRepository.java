package myProject1.gift.repository;

import lombok.RequiredArgsConstructor;
import myProject1.gift.domain.Gift;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class GiftRepository {
    private final EntityManager em;

    public Long save(Gift gift){
        em.persist(gift);
        return gift.getId();
    }

    public Gift findOne(Long giftId){
        Gift resultGift = em.find(Gift.class, giftId);
        return resultGift;
    }
}
