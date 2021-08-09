package myProject1.gift.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "GIFT_BASKETS")
@Getter @Setter
public class GiftBasket {

    @Id @GeneratedValue
    @Column(name = "GIFT_BASKET_ID")
    private Long id;

    @OneToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToOne
    @JoinColumn(name = "RECEIVED_ID")
    private Member receiveMember;

    @OneToOne
    @JoinColumn(name = "GIFT_ID")
    private Gift gift;

    @OneToMany(mappedBy = "giftBasket")
    private List<GiftItem> giftItems = new ArrayList<>();
}
