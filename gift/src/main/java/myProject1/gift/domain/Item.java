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

    private int price;

    private int stockQuantity;

    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    //==생성 메서드==//
    public static Item createItem(String name, int price, int stockQuantity, Category category){
        Item item = new Item();
        item.name = name;
        item.price = price;
        item.stockQuantity = stockQuantity;
        item.category = category;

        return item;
    }
}
