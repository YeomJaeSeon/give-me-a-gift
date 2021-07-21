package myProject1.gift.domain;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Table(name = "GIFT_ITEMS")
public class GiftItem {
    @Id @GeneratedValue
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
}
