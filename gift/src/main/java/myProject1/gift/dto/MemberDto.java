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

@ToString
@Getter @Setter
public class MemberDto {
    @NotEmpty(message = "이름이 공백입니다.")
    private String name;
    @NotEmpty(message = "아이디가 공백입니다.")
    private String username;
    @NotEmpty(message = "비밀번호가 공백입니다")
    private String password;
    private String role;
    private String sex;
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
        member.setRole(Role.valueOf(role));
        member.setSex(SexStatus.valueOf(sex));
        member.setBirthDate(birthDate);
        member.setMessage(message);

        return member;
    }
}
