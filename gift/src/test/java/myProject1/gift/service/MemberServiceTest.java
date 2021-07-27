package myProject1.gift.service;

import myProject1.gift.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    EntityManager em;

    @Test
    void 회원생성_및_조회(){
        //given
        Member member = createMember("memberA", SexStatus.FEMALE, LocalDate.of(1996, 3, 15), "저는 행복합니다");

        //when
        memberService.createMember(member);
        Member resultMember = em.find(Member.class, member.getId());

        //then
        assertThat(member).isEqualTo(resultMember);
    }


    @Test
    void 회원정보수정(){
        //given
        Member member = createMember("memberA", SexStatus.FEMALE, LocalDate.of(1996, 3, 15), "저는 행복합니다");
        memberService.createMember(member);

        Long updateId = member.getId();
        String updateName = "김덕순";
        SexStatus updateSex = SexStatus.MALE;

        //when
//        memberService.updateMember(updateId, updateName, updateSex);
        Member resultMember = em.find(Member.class, updateId);

        //then
        assertThat(resultMember.getName()).isEqualTo(updateName);
        assertThat(resultMember.getSex()).isEqualTo(updateSex);
    }

    @Test
    void 회원삭제(){
        //given
        Member member1 = createMember("member1", SexStatus.MALE, LocalDate.now(), "ㅎㅎ");
        Member member2 = createMember("member2", SexStatus.FEMALE, LocalDate.now(), "안뇽안뇽");

        memberService.createMember(member1);
        memberService.createMember(member2);

        //when
        Member deleteMember = em.find(Member.class, member1.getId());
        memberService.deleteMember(deleteMember);
        List<Member> allMembers = memberService.findAllMembers();

        //then
        assertThat(allMembers.size()).isEqualTo(1);
    }

    @Test
    void 선물받은_회원_조회(){
        //given
        Member member1 = createMember("member1", SexStatus.MALE, LocalDate.now(), "ㅎㅎ");
        Member member2 = createMember("member2", SexStatus.FEMALE, LocalDate.now(), "안뇽안뇽");
        Member member3 = createMember("member3", SexStatus.FEMALE, LocalDate.now(), "vv!!");
        memberService.createMember(member1);
        memberService.createMember(member2);
        memberService.createMember(member3);

        //when
        member1.setStatus(GiftReceiveStatus.RECEIVED);
        List<Member> birthdayMembers = memberService.findSpecificMembers(GiftReceiveStatus.RECEIVED);

        //then
        assertThat(birthdayMembers.size()).isEqualTo(1);
        assertThat(birthdayMembers.get(0).getName()).isEqualTo("member1");
    }

    private Member createMember(String name, SexStatus sex, LocalDate birthDate, String message) {
        Member member = new Member();
        member.setName(name);
        member.setSex(sex);
        member.setBirthDate(birthDate);
        member.setMessage(message);
        return member;
    }

}