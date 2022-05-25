package com.ttnm.chillchatting.services.badword;

import com.ttnm.chillchatting.dtos.MyEnum;
import com.ttnm.chillchatting.dtos.badword.BadWordDto;
import com.ttnm.chillchatting.entities.BadWord;
import com.ttnm.chillchatting.entities.Message;
import com.ttnm.chillchatting.exceptions.InvalidException;
import com.ttnm.chillchatting.exceptions.NotFoundException;
import com.ttnm.chillchatting.repositories.BadWordRepository;
import com.ttnm.chillchatting.services.message.MessageService;
import com.ttnm.chillchatting.utils.EnumChannel;
import com.ttnm.chillchatting.utils.StringUtilsVN;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Slf4j
@Service
public class BadWordServiceImpl implements BadWordService {
    private final BadWordRepository badWordRepository;

    private final MongoTemplate mongoTemplate;

    public BadWordServiceImpl(BadWordRepository badWordRepository, MongoTemplate mongoTemplate) {
        this.badWordRepository = badWordRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public BadWord getBadWordById(String id) {
        return badWordRepository.getById(id)
                .orElseThrow(() -> new NotFoundException("Bad word filter not found"));
    }

    @Override
    public List<BadWord> getAll() {
        return badWordRepository.getAll();
    }

    @Override
    public BadWord createNewBadWord(BadWordDto dto) {
        String bw = StringUtilsVN.removeAccentJava(dto.getBadWord()).toLowerCase(Locale.ROOT);
        if (ObjectUtils.isEmpty(bw))
            throw new InvalidException("Bad word filter cannot be empty");
        if (bw.length() < 3)
            throw new InvalidException("Bad word filter too short");
        if (bw.length() > 10)
            throw new InvalidException("Bad word filter too long");
        if (badWordRepository.checkIfBadWordExists(bw))
            throw new InvalidException("Bad word filter existed");
        BadWord badWord = new BadWord();
        badWord.setBadWord(bw);
        return mongoTemplate.save(badWord);
    }

    @Override
    public BadWord updateBadWord(String id, BadWordDto dto) {
        BadWord badWord = getBadWordById(id);
        String bw = StringUtilsVN.removeAccentJava(dto.getBadWord()).toLowerCase(Locale.ROOT);
        if (ObjectUtils.isEmpty(bw))
            throw new InvalidException("Bad word filter cannot be empty");
        if (bw.length() < 3)
            throw new InvalidException("Bad word filter too short");
        if (bw.length() > 10)
            throw new InvalidException("Bad word filter too long");
        if (!badWord.getBadWord().equals(bw) && badWordRepository.checkIfBadWordExists(bw))
            throw new InvalidException("Bad word filter existed");
        badWord.setBadWord(bw);
        return mongoTemplate.save(badWord);
    }

    @Override
    public void deleteBadWord(String id) {
        BadWord badWord = getBadWordById(id);
        mongoTemplate.remove(badWord);
    }

    @Override
    public String filter(String string) {
        List<BadWord> badWords = getAll();
        String temp = StringUtilsVN.removeAccentJava(string).toLowerCase(Locale.ROOT);
        for (BadWord badWord : badWords) {
            int length = badWord.getBadWord().length();
            String filter = org.apache.commons.lang.StringUtils.repeat("*", badWord.getBadWord().length());
            for (int count = 0; count < temp.length(); count++) {
                int i = temp.substring(count).indexOf(badWord.getBadWord());
                if (i >= 0) {
                    //string = string.replace(string.substring(i + count, i + count + badWord.getBadWord().length()), org.apache.commons.lang.StringUtils.repeat("*", badWord.getBadWord().length()));
                    string = string.substring(0, i + count) + filter + string.substring(i + count + length);
                    count = i + count + length;
                } else break;
            }
        }
        return string;
    }

    @Override
    public String filterWithRegex(String string) {
        List<BadWord> badWords = getAll();
        for (BadWord badWord : badWords) {
            String regex = StringUtilsVN.makeVietNameseRegex(badWord.getBadWord());
            Pattern pattern = Pattern.compile(regex);
            System.out.println(pattern.pattern());
            string = string.replaceAll(pattern.pattern(), org.apache.commons.lang.StringUtils.repeat("*", badWord.getBadWord().length()));
        }
        return string;
    }
}
