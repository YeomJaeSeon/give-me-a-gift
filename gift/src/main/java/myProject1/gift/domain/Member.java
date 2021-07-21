package myProject1.gift.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.*;

@Entity
@Table(name = "MEMBERS")
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    private String name;

    @Enumerated(STRING)
    private SexStatus sex;

    private LocalDate birthDate;

    private String message;

    @Enumerated(STRING)
    private GiftStatus status = GiftStatus.NOT_RECEIVED;


    @OneToMany(mappedBy = "member")
    private List<Gift> gifts = new ArrayList<>();
}
