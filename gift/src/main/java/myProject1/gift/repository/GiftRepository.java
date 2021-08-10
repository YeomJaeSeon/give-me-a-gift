package myProject1.gift.repository;

import lombok.RequiredArgsConstructor;
import myProject1.gift.domain.Gift;
import myProject1.gift.domain.GiftStatus;
import myProject1.gift.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

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

    public List<Gift> findGiftByReceiveMember(Member member){
        List<Gift> receiveGifts = em.createQuery("select g from Gift g where g.receiveMember = :member and g.status <> :status", Gift.class)
                .setParameter("member", member)
                .setParameter("status", GiftStatus.BASKET)
                .getResultList();

        return receiveGifts;
    }

    public int findCreatedGifts(Member member){
        int size = em.createQuery("select g from Gift g where g.receiveMember = :member and g.status = :status", Gift.class)
                .setParameter("member", member)
                .setParameter("status", GiftStatus.CREATED)
                .getResultList()
                .size();
        return size;
    }

}
