package myProject1.gift.service;

import lombok.RequiredArgsConstructor;
import myProject1.gift.domain.GiftReceiveStatus;
import myProject1.gift.domain.Member;
import myProject1.gift.domain.SexStatus;
import myProject1.gift.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    
    @Transactional
    public Long createMember(Member member){
        Long memberId = memberRepository.save(member);
        return memberId;
    }

    @Transactional
    public void updateMember(Long memberId, String updateName, SexStatus updateSex){
        memberRepository.update(memberId, updateName, updateSex);
    }

    @Transactional
    public Long deleteMember(Member member){
        Long deleteMemberId = memberRepository.delete(member);
        return deleteMemberId;
    }

    public List<Member> findAllMembers(){
        return memberRepository.findAll();
    }

    public List<Member> findSpecificMembers(GiftReceiveStatus receivedStatus){
        return memberRepository.findSpecificMembers(receivedStatus);
    }

    public List<Member> findReceivedMembersOrderByGiftCount(){
        return memberRepository.findMembersOrderByGift();
    }
}
