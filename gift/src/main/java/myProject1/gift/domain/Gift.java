package myProject1.gift.domain;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.FetchType.*;

@Entity
@Table(name = "GIFTS")
public class Gift {

    @Id @GeneratedValue
    @Column(name = "GIFT_ID")
    private Long id;

    private LocalDate giftDate;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "MESSAGE_ID")
    private Message message;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "RECEIVED_ID")
    private Member receiveMember;
}
