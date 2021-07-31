package myProject1.gift.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import myProject1.gift.domain.Member;
import myProject1.gift.domain.Role;
import myProject1.gift.domain.SexStatus;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter @Setter
public class MemberDto {
    @NotEmpty(message = "이름이 공백입니다.")
    private String name;

    private String username;
    private String password;
    private String role;

    @NotNull(message = "성별을 선택해주세요")
    private SexStatus sexStatus;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "생일을 선택해주세요")
    private LocalDate birthDate;
    private String message;

    //==MemberDto -> Member Entity==//
    public Member toEntity(){
        Member member = new Member();
        member.setName(name);
        member.setUsername(username);
        member.setPassword(password);
        member.setRole(Role.valueOf(this.role));
        member.setSex(sexStatus);
        member.setBirthDate(birthDate);
        member.setMessage(message);

        return member;
    }
}
