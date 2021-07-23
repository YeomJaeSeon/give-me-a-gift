package myProject1.gift.repository;

import lombok.RequiredArgsConstructor;
import myProject1.gift.domain.Message;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class MessageRepository {
    private final EntityManager em;

    public Long save(Message message){
        em.persist(message);
        return message.getId();
    }

    public Message findOne(Long id){
        Message resultMessage = em.find(Message.class, id);
        return resultMessage;
    }
}
