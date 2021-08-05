package myProject1.gift.repository;

import lombok.RequiredArgsConstructor;
import myProject1.gift.domain.Gift;
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

//    //==로그이한 회원이 생성했고, 해당 선물의 status가 created 인 선물 조회==//
//    public List<Gift> findByGiveUser(Member giveMember){
//        List<Gift> gifts = em.createQuery("select g from Gift g where g.member = :member and g.status = myProject1.gift.domain.GiftStatus.CREATED", Gift.class)
//                .setParameter("member", giveMember)
//                .getResultList();
//
//        return gifts;
//    }
}
