package myProject1.gift.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class CategoryDto {
    @NotBlank(message = "공백은 허용되지 않습니다")
    private String category;
}
