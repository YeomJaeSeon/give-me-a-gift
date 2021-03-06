package myProject1.gift.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "GIFT_ITEMS")
@NoArgsConstructor(access = PROTECTED)
@Getter @Setter
public class GiftItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GIFT_ITEM_ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "GIFT_ID")
    private Gift gift;

    private int price; // 한 종류의 상품의 하나의 가격

    private int count; // 한종류의 상품의 개수

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "BASKET_ID")
    private Basket basket;

    //==생성메서드==//
    public static GiftItem createGiftItem(Item item, int price, int count){
        GiftItem giftItem = new GiftItem();

        giftItem.item = item;
        giftItem.price = price;
        giftItem.count = count;

        return giftItem;
    }

    //==비즈니스 로직==//
    //- 선물 상품 생성
    public void createGiftItem() {
        item.reduceStockQuantity(count);
    }

    //- 선물 상품 거부됨
    public void refused() {
        item.addStockQuantity(count);
    }

    //- 한종류 선물상품의 총 가격
    public int getTotalPrice(){
        return price * count;
    }
}
