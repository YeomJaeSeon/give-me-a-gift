package myProject1.gift.service;

import lombok.RequiredArgsConstructor;
import myProject1.gift.domain.GiftReceiveStatus;
import myProject1.gift.domain.Member;
import myProject1.gift.domain.Role;
import myProject1.gift.domain.SexStatus;
import myProject1.gift.dto.MemberDto;
import myProject1.gift.repository.MemberRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Transactional
    public Long createMember(MemberDto memberDto){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        Member member = memberDto.toEntity();

        Long memberId = memberRepository.save(member);
        return memberId;
    }

    @Transactional
    public void updateMember(String originalName, MemberDto memberDto){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        SexStatus memberDtoSex = SexStatus.valueOf(memberDto.getSex());
        Role role = Role.valueOf(memberDto.getRole());

        //인증 정보 수정
        List<GrantedAuthority> authorities = new ArrayList<>();
        if(role == Role.ADMIN)
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        else
            authorities.add(new SimpleGrantedAuthority(Role.USER.getValue()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(memberDto.getUsername(), memberDto.getPassword(), authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        memberRepository.update(originalName ,memberDto.getUsername(), memberDto.getPassword(), memberDto.getName(), memberDtoSex, role ,memberDto.getBirthDate(), memberDto.getMessage());
    }

    @Transactional
    public Long deleteMember(Member member) {
        Long deleteMemberId = memberRepository.delete(member);
        return deleteMemberId;
    }

    public Member findById(Long memberId){
        Member member = memberRepository.findOne(memberId);
        return member;
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

    public List<Member> findReceivedMembersReverseOrderByGiftCount(){
        return memberRepository.findMembersReverseOrderByGift();
    }

    public List<Member> findBirthDateMembers(){
        return memberRepository.findBirthDateMembers();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<Member> members = memberRepository.findByUsername(username);
        if(members.size() == 0){
            throw new IllegalStateException("존재하지 않는 회원입니다.");
        }
        Member member = members.get(0);

        List<GrantedAuthority> authorities = new ArrayList<>();
        if(member.getRole() == Role.ADMIN)
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        else
            authorities.add(new SimpleGrantedAuthority(Role.USER.getValue()));

        return new User(member.getUsername(), member.getPassword(), authorities);
    }
}
