package myProject1.gift.repository;

import lombok.RequiredArgsConstructor;
import myProject1.gift.domain.GiftReceiveStatus;
import myProject1.gift.domain.Member;
import myProject1.gift.domain.Message;
import myProject1.gift.domain.SexStatus;
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

    public void update(Long memberId, String name, SexStatus sex, LocalDate birthDate, String message){
        Member findMember = em.find(Member.class, memberId);
        findMember.setName(name);
        findMember.setSex(sex);
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
        List<Member> members = em.createQuery("select m from Member m where m.status = :status order by m.gifts.size() asc", Member.class)
                .setParameter("status", GiftReceiveStatus.RECEIVED)
                .getResultList();
        return members;
    }
}
