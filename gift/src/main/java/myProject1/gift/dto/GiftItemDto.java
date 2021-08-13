package myProject1.gift.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter @Setter
public class GiftItemDto {
    private Long itemId;

    private int price;

    @Positive(message = "선물할 개수는 1개 이상이여야합니다")
    @NotNull(message = "공백은 허용되지 않습니다")
    private Integer count;
}
