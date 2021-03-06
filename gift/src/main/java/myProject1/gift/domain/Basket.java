package myProject1.gift.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "BASKETS")
@Getter @Setter
public class Basket {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BASKET_ID")
    private Long id;

    @OneToMany(mappedBy = "basket")
    private List<GiftItem> giftItems = new ArrayList<>();

    @Enumerated(STRING)
    private DeleteFlag deleteFlag = DeleteFlag.EXISTED;

    //==연관관계 편의메서드==//
    public void addGiftItem(GiftItem giftItem){
        giftItems.add(giftItem);
        giftItem.setBasket(this);

        //선물바구니에 선물 담을시, 선물할 상품의 재고 줄이기
        giftItem.createGiftItem();
    }
}
