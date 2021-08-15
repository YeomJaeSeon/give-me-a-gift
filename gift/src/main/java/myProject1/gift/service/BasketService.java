package myProject1.gift.service;

import lombok.RequiredArgsConstructor;
import myProject1.gift.domain.Basket;
import myProject1.gift.domain.Member;
import myProject1.gift.repository.BasketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasketService {
    private final BasketRepository basketRepository;

    @Transactional
    public Long createBasket(Basket basket){
        Long saveId = basketRepository.save(basket);
        return saveId;
    }

    public Basket findBasketById(Long id){
        Basket basket = basketRepository.findById(id);
        return basket;
    }

    public Basket findBasketByMember(Member member){
        Basket basket = basketRepository.findByMember(member);
        return basket;
    }
}
