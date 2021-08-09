package myProject1.gift.service;

import myProject1.gift.domain.GiftBasket;
import myProject1.gift.domain.Member;
import myProject1.gift.dto.MemberDto;
import myProject1.gift.repository.GiftBasketRepository;
import myProject1.gift.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class GiftBasketTest {

    @Autowired
    GiftBasketRepository giftBasketRepository;
    @Autowired
    MemberService memberService;

    //선물바구니에 담기 클릭하면 선물바구니가 생성.. (상대방은 지정하지않아도된다.) - 선물바구니도 선물하기 누를때, 상대방 지정안되면 상대방 지정되게
    @Test
    void 선물바구니_생성(){
        //given
        MemberDto memberDto = createMemberDto("memberA", "yjs", "1234", "USER", "FEMALE", LocalDate.of(1996, 3, 15), "저는 행복해여~");
        Long memberId = memberService.createMember(memberDto);
        Member member = memberService.findById(memberId);

        GiftBasket giftBasket = new GiftBasket();
        giftBasket.setMember(member); //로그인한 회원, 즉 선물을 주는 회원의 아이디만 설정되면 선물바구니 생성

        //when
        Long giftBasketId = giftBasketRepository.save(giftBasket);
        GiftBasket giftBasketOne = giftBasketRepository.findById(giftBasketId);
        List<GiftBasket> giftBaskets = giftBasketRepository.findByMember(member);

        assertThat(giftBaskets.size()).isEqualTo(1);
        assertThat(giftBaskets.get(0)).isEqualTo(giftBasketOne);
    }

    //로그인한 유저의 GiftBasket이 없으면 생성해야하고 있으면 GiftBasket을 조회해야함
    @Test
    void 선물바구니_없을_경우_선물바구니_상품담기(){
        //given
        MemberDto memberDto = createMemberDto("memberA", "yjs", "1234", "USER", "FEMALE", LocalDate.of(1996, 3, 15), "저는 행복해여~");
        Long memberId = memberService.createMember(memberDto);
        Member member = memberService.findById(memberId);

        GiftBasket giftBasket = new GiftBasket();
        giftBasket.setMember(member); //로그인한 회원, 즉 선물을 주는 회원의 아이디만 설정되면 선물바구니 생성

        Long giftBasketId = giftBasketRepository.save(giftBasket);


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
