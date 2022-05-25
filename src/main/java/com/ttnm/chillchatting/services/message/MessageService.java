package com.ttnm.chillchatting.services.message;

import com.ttnm.chillchatting.dtos.EvaluateScoreDto;
import com.ttnm.chillchatting.dtos.MyEnum;
import com.ttnm.chillchatting.dtos.RegistNameRequest;
import com.ttnm.chillchatting.dtos.RegistNameResponse;
import com.ttnm.chillchatting.dtos.message.MessageDto;
import com.ttnm.chillchatting.dtos.statistic.Statistic;
import com.ttnm.chillchatting.entities.EvaluateScore;
import com.ttnm.chillchatting.entities.Message;

import java.util.List;

public interface MessageService{
    RegistNameResponse dangKyTenChat(RegistNameRequest dto);

    Message guiMessage(MessageDto dto);

    List<MyEnum> getKenhs();

    List<Message> getTheLatestMessages(String key);

    Statistic getStatistic();

    EvaluateScore nguoiDungDanhGia(EvaluateScoreDto dto);
}
