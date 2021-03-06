package myProject1.gift.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "ITEMS")
@Getter @Setter
@NoArgsConstructor(access = PROTECTED)
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_ID")
    private Long id;

    private String name;

    private Integer price;

    private Integer stockQuantity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    //==연관관계 편의메서드==//
    public void addItem(Category category){
        category.getItems().add(this);
        this.category = category;
    }

    //==생성 메서드==//
    public static Item createItem(String name, int price, int stockQuantity, Category category){
        Item item = new Item();
        item.name = name;
        item.price = price;
        item.stockQuantity = stockQuantity;
        if(category != null) item.addItem(category);

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
