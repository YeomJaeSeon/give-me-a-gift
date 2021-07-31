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
        MemberDto memberDto1 = createMemberDtoInit("김덕칠", SexStatus.MALE, "안녕", LocalDate.now(), "yjs3819", "1234", "ROLE_ADMIN");
        MemberDto memberDto2 = createMemberDtoInit("김두철", SexStatus.MALE, "", LocalDate.now(), "mj3131", "1234", "ROLE_USER");
        MemberDto memberDto3 = createMemberDtoInit("정덕칠", SexStatus.FEMALE, "안녕zzz", LocalDate.now(), "ymz3910", "12345", "ROLE_USER");

       memberService.createMember(memberDto1);
       memberService.createMember(memberDto2);
       memberService.createMember(memberDto3);
    }

    // == 미리 넣는 데이터==//
    private MemberDto createMemberDtoInit(String name, SexStatus sex, String message, LocalDate date, String username, String password, String role) {
        MemberDto memberDto = new MemberDto();
        memberDto.setName(name);
        memberDto.setUsername(username);
        memberDto.setPassword(password);
        memberDto.setBirthDate(date);
        memberDto.setRole(role);
        memberDto.setSexStatus(sex);
        memberDto.setMessage(message);

        return memberDto;
    }

    //==로그인 페이지 조회==//
    @GetMapping("/login")
    public String dispLogin(){
        return "auth/login";
    }

    //==회원 가입 페이지 조회==//
    @GetMapping("/signup")
    public String createMemberForm(Model model){
        model.addAttribute("form", new MemberDto());
        return "member/createMemberForm";
    }

    //==회원 가입==//
    @PostMapping("/members/new")
    public String createMember(@Valid @ModelAttribute("form") MemberDto memberDTO, BindingResult result){

        if(result.hasErrors()){
            return "member/createMemberForm";
        }

        memberService.createMember(memberDTO);

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
    public String memberEdit(@Valid @ModelAttribute("form") MemberDto memberDto,BindingResult result, @PathVariable Long memberId){
        if(result.hasErrors()){
            return "member/editMemberForm";
        }
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
