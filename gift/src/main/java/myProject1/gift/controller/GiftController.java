package myProject1.gift.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myProject1.gift.repository.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GiftController {
    private final MemberRepository memberRepository;

    //==선물 받을 상대 선택==//
    @GetMapping("/members/{receiveMemberId}/gift")
    public String selectReceiveMember(@PathVariable Long receiveMemberId, HttpServletRequest request){

        HttpSession session = request.getSession();
        session.setAttribute("receiveMemberId", receiveMemberId);

        return "redirect:/";
    }

}
