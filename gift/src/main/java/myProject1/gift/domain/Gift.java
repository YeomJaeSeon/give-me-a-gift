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

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "MESSAGE_ID")
    private Message message;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "RECEIVED_ID")
    private Member receiveMember;

    @OneToMany(mappedBy = "gift")
    private List<GiftItem> giftItems = new ArrayList<>();

    @Enumerated(STRING)
    private GiftStatus status;

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
    //- 선물 생성
    public static Gift createGift(LocalDate giftDate, Message message, Member member, Member receiveMember, List<GiftItem> giftItems){
        Gift gift = new Gift();

        gift.giftDate = giftDate;
        gift.message = message;
        gift.addMember(member);
        gift.receiveMember = receiveMember;
        gift.status = GiftStatus.CREATED;

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
