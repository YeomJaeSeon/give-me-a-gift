package myProject1.gift.domain;

import lombok.Getter;
import lombok.Setter;
import myProject1.gift.dto.MemberDto;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "MEMBERS")
@Getter @Setter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    private String name;

    @Column(unique = true)
    private String username;

    private String password;

    @Enumerated(STRING)
    private Role role;

    @Enumerated(STRING)
    private SexStatus sex;

    private LocalDate birthDate;

    private String message;

    @Enumerated(STRING)
    private GiftReceiveStatus status = GiftReceiveStatus.NOT_RECEIVED; // default Value

    @OneToMany(mappedBy = "member")
    private List<Gift> gifts = new ArrayList<>();

    @OneToMany(mappedBy = "receiveMember")
    private List<Gift> receiveGifts = new ArrayList<>();
}
