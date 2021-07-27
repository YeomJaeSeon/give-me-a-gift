package myProject1.gift.dto;

import lombok.Getter;
import lombok.Setter;
import myProject1.gift.domain.Category;

@Getter @Setter
public class ItemDto {
    private String name;
    private Long category;
    private int price;
    private int stockQuantity;

    @Override
    public String toString() {
        return "ItemDto{" +
                "name='" + name + '\'' +
                ", category=" + category +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                '}';
    }
}
