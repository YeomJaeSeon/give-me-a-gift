package myProject1.gift.repository;

import lombok.RequiredArgsConstructor;
import myProject1.gift.domain.Basket;
import myProject1.gift.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class BasketRepository {
    private final EntityManager em;

    public Long save(Basket basket){
        em.persist(basket);
        return basket.getId();
    }

    public Basket findById(Long id){
        Basket basket = em.find(Basket.class, id);
        return basket;
    }

    public Basket findByMember(Member member){
        Basket basket = em.createQuery("select b from Basket b where b.member = :member", Basket.class)
                .setParameter("member", member)
                .getSingleResult();
        return basket;
    }
}
