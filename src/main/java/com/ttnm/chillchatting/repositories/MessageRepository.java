package com.ttnm.chillchatting.repositories;

import com.ttnm.chillchatting.entities.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {

    @Query(value = "{'guestName':?0}",exists = true)
    boolean checkIfGuestNameExists(String guestName);

    @Query(value = "{'channel':?0}")
    List<Message> getListMessageWithLimit(String kenh, Pageable pageable);

}
