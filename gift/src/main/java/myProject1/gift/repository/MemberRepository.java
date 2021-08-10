package myProject1.gift.repository;

import lombok.RequiredArgsConstructor;
import myProject1.gift.domain.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final EntityManager em;

    public Long save(Member member){
        em.persist(member);
        return member.getId();
    }

    public void update(String originalName , String username, String password, String name, SexStatus sex, Role role, LocalDate birthDate, String message){
        List<Member> members = em.createQuery("select m from Member m where m.username = :originalName", Member.class)
                .setParameter("originalName", originalName)
                .getResultList();
        Member findMember = members.get(0);
        findMember.setUsername(username);
        findMember.setPassword(password);
        findMember.setName(name);
        findMember.setSex(sex);
        findMember.setRole(role);
        findMember.setBirthDate(birthDate);
        findMember.setMessage(message);
    }

    public Long delete(Member member){
        Long deleteId = member.getId();
        em.remove(member);
        return deleteId;
    }

    public List<Member> findAll(){
        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();
        return members;
    }

    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    //==username으로 멤버 찾기==//
    public List<Member> findByUsername(String username){
        List<Member> members = em.createQuery("select m from Member m where m.username=:username", Member.class)
                .setParameter("username", username)
                .getResultList();
        return members;
    }

    //선물을 받았거나, 받지않은 멤버 찾기
    public List<Member> findSpecificMembers(GiftReceiveStatus receivedStatus){
        List<Member> members = em.createQuery("select m from Member m where m.status = :status", Member.class)
                .setParameter("status", receivedStatus)
                .getResultList();
        return members;
    }

    // 선물 많이 받은순으로 조회
    public List<Member> findMembersOrderByGift(){
        List<Member> members = em.createQuery("select m from Member m order by m.receiveGifts.size desc", Member.class)
                .getResultList();
        return members;
    }

    //선물 적게 받은순으로 조회
    public List<Member> findMembersReverseOrderByGift(){
        List<Member> members = em.createQuery("select m from Member m order by m.receiveGifts.size asc", Member.class)
                .getResultList();
        return members;
    }

    //생일인 회원 조회
    public List<Member> findBirthDateMembers(){
        List<Member> members = em.createQuery("select m from Member m where m.birthDate = :birthDate", Member.class)
                .setParameter("birthDate", LocalDate.now())
                .getResultList();
        return members;
    }
}
