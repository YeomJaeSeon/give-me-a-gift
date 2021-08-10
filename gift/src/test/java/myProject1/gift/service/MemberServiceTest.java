package myProject1.gift.service;

import myProject1.gift.domain.*;
import myProject1.gift.dto.MemberDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
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
        MemberDto memberDto = createMemberDto("memberA", "yjs", "1234", "USER", "FEMALE", LocalDate.of(1996, 3, 15), "저는 행복해여~");

        //when
        Long memberId = memberService.createMember(memberDto);
        Member resultMember = em.find(Member.class, memberId);

        //then
        assertThat(memberDto.getName()).isEqualTo(resultMember.getName());
        assertThat(memberDto.getSex()).isEqualTo(resultMember.getSex().getValue());
        assertThat(memberDto.getMessage()).isEqualTo(resultMember.getMessage());
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


    @Test
    void 회원정보수정(){
        //given
        MemberDto memberDto = createMemberDto("memberA","yeom", "1234","USER", "FEMALE", LocalDate.of(1996, 3, 15), "저는 행복해여~");
        Long updateId = memberService.createMember(memberDto);

        String updateName = "김덕순";
        memberDto.setName("김덕순");
        memberDto.setSex("MALE");
        memberDto.setRole("USER");

        //when
        memberService.updateMember("yeom", memberDto);
        Member resultMember = em.find(Member.class, updateId);

        //then
        assertThat(resultMember.getName()).isEqualTo(updateName);
        assertThat(resultMember.getSex()).isEqualTo(SexStatus.MALE);
    }

    @Test
    void 회원삭제(){
        //given
        MemberDto memberDto1 = createMemberDto("member1", "yeom", "1234", "USER", "MALE", LocalDate.now(), "ㅎㅎ");
        MemberDto memberDto2 = createMemberDto("member2", "yeom1", "1234", "USER", "FEMALE", LocalDate.now(), "안뇽안뇽");

        Long memberId1 = memberService.createMember(memberDto1);
        Long memberId2 = memberService.createMember(memberDto2);

        //when
        Member deleteMember = em.find(Member.class, memberId1);
        memberService.deleteMember(deleteMember);
        List<Member> allMembers = memberService.findAllMembers();

        //then
        assertThat(allMembers.size()).isEqualTo(1);
    }

    @Test
    void 선물받은_회원_조회(){
        //given
        MemberDto member1 = createMemberDto("member1", "yeom", "1234", "USER", "MALE", LocalDate.now(), "ㅎㅎ");
        MemberDto member2 = createMemberDto("member2", "yeom1", "1234", "USER", SexStatus.FEMALE.getValue(), LocalDate.now(), "안뇽안뇽");
        MemberDto member3 = createMemberDto("member3", "yeom2", "1234", "USER", SexStatus.FEMALE.getValue(), LocalDate.now(), "vv!!");
        Long memberId = memberService.createMember(member1);
        memberService.createMember(member2);
        memberService.createMember(member3);
        Member member = em.find(Member.class, memberId);

        //when
        member.setStatus(GiftReceiveStatus.RECEIVED);
        List<Member> birthdayMembers = memberService.findSpecificMembers(GiftReceiveStatus.RECEIVED);

        //then
        assertThat(birthdayMembers.size()).isEqualTo(1);
        assertThat(birthdayMembers.get(0).getName()).isEqualTo("member1");
    }

    @Test
    void 생일인_회원_조회(){
        //given
        MemberDto member1 = createMemberDto("member1", "yeom", "1234", "USER", "MALE", LocalDate.of(2021, 8, 10), "ㅎㅎ");
        MemberDto member2 = createMemberDto("member2", "yeom1", "1234", "USER", SexStatus.FEMALE.getValue(), LocalDate.of(2012, 3, 1), "안뇽안뇽");
        MemberDto member3 = createMemberDto("member3", "yeom2", "1234", "USER", SexStatus.FEMALE.getValue(), LocalDate.of(1996, 3, 7), "vv!!");

        memberService.createMember(member1);
        memberService.createMember(member2);
        memberService.createMember(member3);

        //when
        List<Member> members = memberService.findBirthDateMembers();

        //then
        assertThat(members.size()).isEqualTo(1);
        assertThat(members.get(0).getName()).isEqualTo("member1");
    }

}