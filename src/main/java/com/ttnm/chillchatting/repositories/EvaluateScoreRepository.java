package com.ttnm.chillchatting.repositories;

import com.ttnm.chillchatting.entities.EvaluateScore;
import com.ttnm.chillchatting.entities.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface EvaluateScoreRepository extends MongoRepository<EvaluateScore, String> {

}
