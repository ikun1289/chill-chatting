package com.ttnm.chillchatting.repositories;

import com.ttnm.chillchatting.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String > {

    @Query(value = "{'_id': ?0}")
    Optional<User> findById(String id);

    @Query(value = "{'userName': ?0}")
    Optional<User> findByUserName(String userName);

    @Query(value = "{$and: [{'_id': ?0}, {'enable' : true }] }")
    Optional<User> findByIdCore(String id);

    @Query(value = "{$and: [{'userName': ?0}, {'enable' : true }] }")
    Optional<User> findByUserNameCore(String userName);
}
