package myProject1.gift.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "MESSAGES")
@Getter @Setter
public class Message {

    @Id @GeneratedValue
    @Column(name = "MESSAGE_ID")
    private Long id;

    private String content = "제 마음입니다."; // default Value
}
