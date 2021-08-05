package myProject1.gift.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.*;
import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@Table(name = "GIFTS")
@NoArgsConstructor(access = PRIVATE)
@Getter
public class Gift {

    @Id @GeneratedValue
    @Column(name = "GIFT_ID")
    private Long id;

    private LocalDate giftDate;

    private String message;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "RECEIVED_ID")
    private Member receiveMember;

    @OneToMany(mappedBy = "gift")
    private List<GiftItem> giftItems = new ArrayList<>();

    @Enumerated(STRING)
    private GiftStatus status = GiftStatus.CREATED; // 선물생성하면 default 상태

    //==연관관계 편의메서드==//
    //회원 - 상품 연관관계
    public void addMember(Member member){
        member.getGifts().add(this);
        this.member = member;
    }

    //선물 - 선물상품
    public void addGiftItem(GiftItem giftItem){
        this.giftItems.add(giftItem);
        giftItem.setGift(this);
    }

    //==생성 메서드==//
    //- 받는사람 주는사람만으로 선물생성
    public static Gift createGiftByUsername(Member giveMember, Member receiveMember){
        Gift gift = new Gift();
        gift.addMember(giveMember);
        gift.receiveMember = receiveMember;

        return gift;
    }

    //- 선물 생성
    public static Gift createGift(LocalDate giftDate, String message ,Member member, Member receiveMember, List<GiftItem> giftItems){
        Gift gift = new Gift();

        gift.giftDate = giftDate;
        gift.message = message;
        gift.addMember(member);
        gift.receiveMember = receiveMember;

        for (GiftItem giftItem : giftItems) {
            gift.addGiftItem(giftItem);
        }

        return gift;
    }

    //==비즈니스 로직==//
    //- 선물 수락
    public void acceptGift(){
        for (GiftItem giftItem : giftItems) {
            giftItem.accepted();
        }
        status = GiftStatus.ACCEPTED;
        receiveMember.setStatus(GiftReceiveStatus.RECEIVED); // 선물받은 회원의 상태 - RECEIVED로변경
    }

    //- 선물 거부
    public void refuseGift(){
//        for (GiftItem giftItem : giftItems) {
//            giftItem.refused();
//        }
        status = GiftStatus.NOT_ACCEPTED;
        receiveMember.setStatus(GiftReceiveStatus.NOT_RECEIVED); //  선물받은 회원의 상태 - NOT_RECEIVED로 변경(필요없을수도! 잠시만!)
    }

    //- 선물 총 가격 조회 메서드
    public int getTotalPrice(){
        return giftItems.stream()
                .mapToInt(GiftItem::getTotalPrice)
                .sum();
    }
}
