package myProject1.gift.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myProject1.gift.domain.SexStatus;
import myProject1.gift.dto.MemberDto;
import myProject1.gift.domain.Member;
import myProject1.gift.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostConstruct
    public void init(){
        Member member = createMemberInit("memberA", SexStatus.MALE, "안뇽낭욘");
        memberService.createMember(member);
        Member member2 = createMemberInit("memberB", SexStatus.FEMALE, "하이");
        memberService.createMember(member2);
    }

    // == 미리 넣는 데이터==//
    private Member createMemberInit(String name, SexStatus sex, String message) {
        Member member = new Member();
        member.setName(name);
        member.setSex(sex);
        member.setBirthDate(LocalDate.now());
        member.setMessage(message);
        return member;
    }

    //==회원 가입 페이지 조회==//
    @GetMapping("/members/new")
    public String createMemberForm(Model model){
        model.addAttribute("form", new MemberDto());
        return "member/createMemberForm";
    }

    //==회원 가입==//
    @PostMapping("/members/new")
    public String createMember(@Valid @ModelAttribute("form") MemberDto memberDTO, BindingResult result){
        log.info("memberDTO : {}", memberDTO);

        if(result.hasErrors()){
            return "member/createMemberForm";
        }
        Member member = new Member();
        member.setName(memberDTO.getName());
        member.setSex(memberDTO.getSexStatus());
        member.setBirthDate(memberDTO.getBirthDate());
        member.setMessage(memberDTO.getMessage());
        memberService.createMember(member);

        return "redirect:/";
    }

    //==회원 목록 페이지 조회==//
    @GetMapping("/members")
    public String memberLists(Model model){
        List<Member> members = memberService.findAllMembers();
        model.addAttribute("members", members);
        return "member/memberList";
    }

    //==회원수정 페이지 조회==//
    @GetMapping("/members/{memberId}/edit")
    public String memberEditForm(Model model, @PathVariable Long memberId){
        Member member = memberService.findById(memberId);

        MemberDto memberDto = new MemberDto();
        memberDto.setName(member.getName());
        memberDto.setSexStatus(member.getSex());
        memberDto.setBirthDate(member.getBirthDate());
        memberDto.setMessage(member.getMessage());

        model.addAttribute("form", memberDto);
        return "member/editMemberForm";
    }

    //==회원 수정==//
    @PostMapping("/members/{memberId}/edit")
    public String memberEdit(@ModelAttribute MemberDto memberDto, @PathVariable Long memberId){
        log.info("memberDto = {}", memberDto);
        memberService.updateMember(memberId, memberDto.getName(), memberDto.getSexStatus(), memberDto.getBirthDate(), memberDto.getMessage());
        return "redirect:/members";
    }

    //==회원 삭제==//
    @GetMapping("/members/{memberId}/delete")
    public String deleteMember(@PathVariable Long memberId) {
        Member findMember = memberService.findById(memberId);
        memberService.deleteMember(findMember);
        return "redirect:/members";
    }
}
