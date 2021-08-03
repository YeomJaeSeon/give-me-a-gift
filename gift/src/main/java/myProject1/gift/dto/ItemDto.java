package myProject1.gift.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class ItemDto {
    @NotBlank(message = "빈 이름은 허용되지 않습니다.")
    private String name;
    private Long category;
    @Range(min=-1, message = "가격은 음수일수 없습니다!")
    @NotNull(message = "공백은 허용되지 않습니다.")
    private Integer price;

    @Range(min=1, message = "재고는 0보다 커야합니다!")
    @NotNull(message = "공백은 허용되지 않습니다.")
    private Integer stockQuantity;


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
