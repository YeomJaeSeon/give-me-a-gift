package myProject1.gift.DTO;

import lombok.Getter;
import lombok.Setter;
import myProject1.gift.domain.SexStatus;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter @Setter
public class MemberDTO {
    @NotEmpty(message = "이름이 공백입니다.")
    private String name;
    @NotNull(message = "성별을 선택해주세요")
    private SexStatus sexStatus;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "생일을 선택해주세요")
    private LocalDate birthDate;
    private String message;

    @Override
    public String toString() {
        return "MemberDTO{" +
                "name='" + name + '\'' +
                ", sexStatus=" + sexStatus +
                ", birthDate=" + birthDate +
                ", message='" + message + '\'' +
                '}';
    }
}
