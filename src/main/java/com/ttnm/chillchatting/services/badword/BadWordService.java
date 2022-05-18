package com.ttnm.chillchatting.services.badword;

import com.ttnm.chillchatting.dtos.badword.BadWordDto;
import com.ttnm.chillchatting.entities.BadWord;

import java.util.List;

public interface BadWordService {
    BadWord getBadWordById(String id);

    List<BadWord> getAll();

    BadWord createNewBadWord(BadWordDto dto);

    BadWord updateBadWord(String id, BadWordDto dto);

    void deleteBadWord(String id);

    String filter(String string);
}
