package myProject1.gift.service;

import myProject1.gift.domain.Basket;
import myProject1.gift.domain.GiftItem;
import myProject1.gift.domain.Item;
import myProject1.gift.domain.Member;
import myProject1.gift.dto.MemberDto;
import myProject1.gift.repository.BasketRepository;
import myProject1.gift.repository.GiftItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Transactional
public class BasketServiceTest {

    @Autowired
    BasketRepository basketRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    ItemService itemService;
    @Autowired
    GiftItemRepository giftItemRepository;
    @Autowired
    EntityManager em;

    @Test
    void 장바구니_추가(){
        //given
        MemberDto memberDto = createMemberDto("memberA", "yjs", "1234", "USER", "FEMALE", LocalDate.of(1996, 3, 15), "저는 행복해여~");
        memberService.createMember(memberDto); //회원생성

        Item item = itemService.findById(1L);

        Member member = memberService.findById(1L);

        GiftItem giftItem = GiftItem.createGiftItem(item, item.getPrice(), 3);
        giftItemRepository.save(giftItem);

        //when
        Basket basket = new Basket();
        basket.setMember(member);
        basket.getGiftItems().add(giftItem);
        giftItem.setBasket(basket);
        basketRepository.save(basket);

        List<Basket> baskets = em.createQuery("select b from Basket  b", Basket.class)
                .getResultList();
        //then
        Assertions.assertThat(baskets.size()).isEqualTo(1);
    }

    @Test
    void 선물바구니의_선물상품_모두찾기(){
        //given
        MemberDto memberDto = createMemberDto("memberA", "yjs", "1234", "USER", "FEMALE", LocalDate.of(1996, 3, 15), "저는 행복해여~");
        Long memberId = memberService.createMember(memberDto);//회원생성
        Member member = memberService.findById(memberId);

        Item item = itemService.findById(1L);
        Item item1 = itemService.findById(2L);

        GiftItem giftItem = GiftItem.createGiftItem(item, item.getPrice(), 3);
        GiftItem giftItem1 = GiftItem.createGiftItem(item, item.getPrice(), 5);
        GiftItem giftItem2 = GiftItem.createGiftItem(item1, item1.getPrice(), 10);

        giftItemRepository.save(giftItem);
        giftItemRepository.save(giftItem1);
        giftItemRepository.save(giftItem2);

        //when
        Basket basket = new Basket();
        basket.setMember(member);
        basket.getGiftItems().add(giftItem);
        basket.getGiftItems().add(giftItem1);
        basket.getGiftItems().add(giftItem2);
        giftItem.setBasket(basket);
        giftItem1.setBasket(basket);
        giftItem2.setBasket(basket);
        basketRepository.save(basket);


        List<Basket> baskets = em.createQuery("select b from Basket b", Basket.class)
                .getResultList();

        //then
        Assertions.assertThat(baskets.size()).isEqualTo(1);
        Assertions.assertThat(baskets.get(0).getGiftItems()).contains(giftItem, giftItem1, giftItem2);
    }

    private MemberDto createMemberDto(String name, String username, String password, String role ,String sex, LocalDate date, String message) {
        MemberDto memberDto = new MemberDto();
        memberDto.setName(name);
        memberDto.setUsername(username);
        memberDto.setPassword(password);
        memberDto.setRole(role);
        memberDto.setSex(sex);
        memberDto.setBirthDate(date);
        memberDto.setMessage(message);
        return memberDto;
    }
}
