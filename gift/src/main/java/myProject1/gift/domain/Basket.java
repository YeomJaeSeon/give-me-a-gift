package myProject1.gift.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Table(name = "BASKETS")
@Getter @Setter
public class Basket {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BASKET_ID")
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "basket")
    private List<GiftItem> giftItems = new ArrayList<>();

    //==연관관계 편의메서드==//
    public void addGiftItem(GiftItem giftItem){
        giftItems.add(giftItem);
        giftItem.setBasket(this);
    }
}
