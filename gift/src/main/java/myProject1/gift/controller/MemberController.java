package myProject1.gift.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myProject1.gift.DTO.MemberDTO;
import myProject1.gift.domain.Member;
import myProject1.gift.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    //==회원 가입 페이지 조회==//
    @GetMapping("/members/new")
    public String createMemberForm(Model model){
        model.addAttribute("form", new MemberDTO());
        return "member/createMemberForm";
    }

    //==회원 가입==//
    @PostMapping("/members/new")
    public String createMember(@Valid @ModelAttribute("form") MemberDTO memberDTO, BindingResult result){
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
}
