package com.ttnm.chillchatting.repositories;

import com.ttnm.chillchatting.entities.BadWord;
import com.ttnm.chillchatting.entities.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BadWordRepository extends MongoRepository<BadWord, String> {

    @Query(value = "{'badWord':?0}",exists = true)
    boolean checkIfBadWordExists(String badWord);

    @Query(value = "{}")
    List<BadWord> getAll();

    @Query(value = "{'_id':?0}")
    Optional<BadWord> getById(String id);

}
