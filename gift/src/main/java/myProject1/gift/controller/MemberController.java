package myProject1.gift.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myProject1.gift.domain.Member;
import myProject1.gift.dto.MemberDto;
import myProject1.gift.repository.MemberRepository;
import myProject1.gift.service.MemberService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final MemberRepository memberRepository;

    //==회원가입 페이지 display==//
    @GetMapping("/signup")
    public String dispSignup(Model model){
        model.addAttribute("memberDto", new MemberDto());
        return "member/signup";
    }

    //==회원가입==//
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute MemberDto memberDto, BindingResult result){
        if(result.hasErrors()){
            return "member/signup";
        }
        log.info("memberDto : {}", memberDto);
        memberService.createMember(memberDto);

        return "redirect:/user/login";
    }

    //==로그인 페이지 display==//
    @GetMapping("/user/login")
    public String dispLogin(){
        return "member/login";
    }

    //==회원 정보 페이지 display==//
    @GetMapping("/user")
    public String dispUserPage(){
        return "member/user";
    }

    //==회원 정보 수정 페이지 display==//
    @GetMapping("/user/edit")
    public String dispEditPage(Model model){
        //현재 로그인한 유저의 정보를 가져오기
        List<Member> members = getLoginedMember();

        Member loginMember = members.get(0);

        //member to memberDto
        MemberDto memberDto = loginMember.toDto();

        log.info("memberDto info : {}", memberDto);

        model.addAttribute("memberDto", memberDto);

        return "member/userEditForm";
    }

    //==회원 정보 수정==//
    @PostMapping("/user/edit")
    public String editUser(@ModelAttribute MemberDto memberDto){
        //현재 로그인한 유저의 정보를 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String originalName = authentication.getName();

        //DB에 회원정보 수정
        memberService.updateMember(originalName ,memberDto);

        return "redirect:/user/info";
    }

    //==내 정보 페이지 display==//
    @GetMapping("/user/info")
    public String dispMyInfo(Model model){
        //현재 로그인한 유저의 정보를 가져오기
        List<Member> members = getLoginedMember();

        Member loginMember = members.get(0);

        //member to memberDto
        MemberDto memberDto = loginMember.toDto();

        model.addAttribute("memberDto", memberDto);

        return "member/info";
    }


    //==회원 삭제==//
    @GetMapping("/user/delete")
    public String deleteUser(){
        //현재 로그인한 유저의 정보를 가져오기
        List<Member> members = getLoginedMember();

        Member loginMember = members.get(0);
        memberService.deleteMember(loginMember);

        return "redirect:/user/logout";
    }



    //============ sub methods (not controller) =================//

    //==현재 로그인한 회원정보 가져오는 메서드==//
    private List<Member> getLoginedMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return memberRepository.findByUsername(username);
    }

}
