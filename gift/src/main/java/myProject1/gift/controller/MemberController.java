package myProject1.gift.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myProject1.gift.domain.GiftReceiveStatus;
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
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    public String signup(@Valid @ModelAttribute MemberDto memberDto, BindingResult result, Model model){
        if(result.hasErrors()){
            //memberDto validation
            return "member/signup";
        }
        //id 중복검사
        List<Member> members = memberRepository.findByUsername(memberDto.getUsername());
        if(members.size() > 0){
            model.addAttribute("duplicateIdError", true);
            return "member/signup";
        }
        log.info("create memberDto : {}", memberDto);
        memberService.createMember(memberDto);

        return "redirect:/user/login";
    }

    //==로그인 페이지 display==//
    @GetMapping("/user/login")
    public String dispLogin(){
        return "member/login";
    }

    //==로그인 오류 페이지 display==//
    @GetMapping(value = "/user/login", params = "error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
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

        //member entity to memberDto
        MemberDto memberDto = new MemberDto();
        memberDto.setUsername(loginMember.getUsername());
        memberDto.setPassword(loginMember.getPassword());
        memberDto.setName(loginMember.getName());
        memberDto.setRole(loginMember.getRole().getValue());
        memberDto.setSex(loginMember.getSex().getValue());
        memberDto.setBirthDate(loginMember.getBirthDate());
        memberDto.setMessage(loginMember.getMessage());

        log.info("memberDto info : {}", memberDto);
        model.addAttribute("memberDto", memberDto);

        return "member/userEditForm";
    }

    //==회원 정보 수정==//
    @PostMapping("/user/edit")
    public String editUser(@Valid @ModelAttribute MemberDto memberDto, BindingResult result){
        log.info("edit memberDto의 role ={}", memberDto.getRole());
        if(result.hasErrors()){
            return "member/userEditForm";
        }
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

        //member entity to memberDto
        MemberDto memberDto = new MemberDto();
        memberDto.setUsername(loginMember.getUsername());
        memberDto.setPassword(loginMember.getPassword());
        memberDto.setName(loginMember.getName());
        memberDto.setRole(loginMember.getRole().getValue());
        memberDto.setSex(loginMember.getSex().getValue());
        memberDto.setBirthDate(loginMember.getBirthDate());
        memberDto.setMessage(loginMember.getMessage());

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

    //==회원 목록 페이지 display==//
    @GetMapping("/members")
    public String dispMembers(Model model, HttpServletRequest request, @RequestParam(required = false) String search){
        List<Member> members = null;
        if(search == null) members = memberService.findAllMembers();
        else{
            if(search.equals("all")) members = memberService.findAllMembers();
            else if(search.equals("birthDate")) members = memberService.findBirthDateMembers();
            else if(search.equals("noGift")) members = memberService.findSpecificMembers(GiftReceiveStatus.NOT_RECEIVED); //선물 받지않은
            else if(search.equals("yesGift")) members = memberService.findSpecificMembers(GiftReceiveStatus.RECEIVED); // 선물 받은
            else if(search.equals("ranking")) members = memberService.findReceivedMembersOrderByGiftCount();
            else if(search.equals("reverseRanking")) members = memberService.findReceivedMembersReverseOrderByGiftCount();
        }

        if(search != null) model.addAttribute("option", search);

        HttpSession session = request.getSession();
        Long receiveMemberId = (Long) session.getAttribute("receiveMemberId");

        model.addAttribute("receiveMember", receiveMemberId);
        model.addAttribute("members", members);

        return "member/members";
    }


    //============ sub methods (not controller) =================//

    //==현재 로그인한 회원정보 가져오는 메서드==//
    private List<Member> getLoginedMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<Member> members = memberRepository.findByUsername(username);
        log.info("로그인한 회원들 : {}", members);
        return members;
    }

}
