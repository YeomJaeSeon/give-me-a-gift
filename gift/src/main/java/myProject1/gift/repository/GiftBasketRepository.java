package myProject1.gift.repository;

import lombok.RequiredArgsConstructor;
import myProject1.gift.domain.GiftBasket;
import myProject1.gift.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GiftBasketRepository {
    private final EntityManager em;

    public Long save(GiftBasket giftBasket){
        em.persist(giftBasket);
        return giftBasket.getId();
    }

    public GiftBasket findById(Long id){
        GiftBasket giftBasket = em.find(GiftBasket.class, id);
        return giftBasket;
    }

    public List<GiftBasket> findByMember(Member member){
        List<GiftBasket> giftBaskets = em.createQuery("select b from GiftBasket b where b.member = :member", GiftBasket.class)
                .setParameter("member", member)
                .getResultList();
        return giftBaskets;
    }

    public Long remove(GiftBasket giftBasket){
        Long deleteId = giftBasket.getId();
        em.remove(giftBasket);
        return deleteId;
    }
}
