package myProject1.gift.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BASKETS")
@Getter
public class Basket {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BASKET_ID")
    private Long id;

    private int price;

    private int count;

    @OneToMany(mappedBy = "basket")
    private List<Item> items = new ArrayList<>();
}
