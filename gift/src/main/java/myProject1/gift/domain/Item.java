package myProject1.gift.domain;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@Table(name = "ITEMS")
@Getter @Setter
@NoArgsConstructor(access = PRIVATE)
public class Item {

    @Id @GeneratedValue
    @Column(name = "ITEM_ID")
    private Long id;

    private String name;

    private Integer price;

    private Integer stockQuantity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    //==생성 메서드==//
    public static Item createItem(String name, int price, int stockQuantity, Category category){
        Item item = new Item();
        item.name = name;
        item.price = price;
        item.stockQuantity = stockQuantity;
        item.category = category; // ... ***

        return item;
    }

    //==비즈니스로직==//
    //- 재고 증가
    public void addStockQuantity(int count) {
        stockQuantity += count;
    }

    //==비즈니스로직==//
    //- 재고 감소
    public void reduceStockQuantity(int count){
        int reduceResult = stockQuantity - count;
        if(reduceResult < 0){
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        stockQuantity = reduceResult;
    }
}
