package myProject1.gift.repository;

import lombok.RequiredArgsConstructor;
import myProject1.gift.domain.GiftStatus;
import myProject1.gift.domain.Member;
import myProject1.gift.domain.SexStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final EntityManager em;

    public Long save(Member member){
        em.persist(member);
        return member.getId();
    }

    public void update(Long memberId, String name, SexStatus sex){
        Member findMember = em.find(Member.class, memberId);
        findMember.setName(name);
        findMember.setSex(sex);
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

    //생일인 멤버 찾기
    public List<Member> findSpecificMembers(){
        List<Member> members = em.createQuery("select m from Member m where m.status = :status", Member.class)
                .setParameter("status", GiftStatus.RECEIVED)
                .getResultList();
        return members;
    }
}
