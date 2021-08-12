package myProject1.gift.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Slf4j
@Entity
@Table(name = "GIFTS")
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString
public class Gift {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "GIFT_ITEM_ID")
    private GiftItem giftItem;

    @Enumerated(STRING)
    private GiftStatus status;

    //==연관관계 편의메서드==//
    //주는 회원 - 선물 연관관계
    public void addMember(Member member){
        member.getGifts().add(this);
        this.member = member;
    }

    public void addReceiveMember(Member member){
        member.getReceiveGifts().add(this);
        this.receiveMember = member;
    }

    //선물 - 선물상품
    public void addGiftItem(GiftItem giftItem){
        this.giftItem = giftItem;
        giftItem.setGift(this);
    }

    //==생성 메서드==//
    //- 선물 생성
    public static Gift createGift(LocalDate giftDate, String message ,Member member, Member receiveMember, GiftItem giftItem){
        Gift gift = new Gift();

        gift.giftDate = giftDate;
        gift.message = message;

        gift.addMember(member); //선물 - 주는사람 연관관계 값 설정
        gift.addReceiveMember(receiveMember);
        receiveMember.setStatus(GiftReceiveStatus.RECEIVED); // 선물받은 회원의 상태 - RECEIVED로변경
        gift.status = GiftStatus.CREATED;

        gift.addGiftItem(giftItem);
        giftItem.createGiftItem();//선물 생성하며 그만큼 재고 줄어듬.

        return gift;
    }

    //==비즈니스 로직==//
    //- 선물 수락
    public void acceptGift(){
        status = GiftStatus.ACCEPTED;
    }

    //- 선물 거부
    public void refuseGift(){
        giftItem.refused();
        status = GiftStatus.NOT_ACCEPTED;
    }

    //- 선물 총 가격 조회 메서드
    public int getTotalPrice(){
        return giftItem.getTotalPrice();
    }
}
