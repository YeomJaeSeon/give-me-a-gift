package myProject1.gift.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myProject1.gift.domain.Member;
import myProject1.gift.repository.MemberRepository;
import myProject1.gift.service.GiftService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GiftController {
    private final GiftService giftService;
    private final MemberRepository memberRepository;

    //==선물 받을 상대 선택==//
//    @GetMapping("/members/{memberId}/gift")
//    public String selectReceiveMember(@PathVariable Long memberId){
//        List<Member> members = getLoginedMember();
//        Member loginMember = members.get(0);
//
//    }

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
