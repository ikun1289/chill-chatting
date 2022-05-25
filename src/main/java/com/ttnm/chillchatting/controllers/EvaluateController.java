package com.ttnm.chillchatting.controllers;


import com.ttnm.chillchatting.dtos.EvaluateScoreDto;
import com.ttnm.chillchatting.dtos.MyEnum;
import com.ttnm.chillchatting.dtos.RegistNameRequest;
import com.ttnm.chillchatting.dtos.RegistNameResponse;
import com.ttnm.chillchatting.dtos.message.MessageDto;
import com.ttnm.chillchatting.entities.EvaluateScore;
import com.ttnm.chillchatting.entities.Message;
import com.ttnm.chillchatting.services.message.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@EnableScheduling
@RestController
@RequestMapping("/rest/danh-gia")
public class EvaluateController {

    private final MessageService messageService;

    private final SimpMessagingTemplate template;

    public EvaluateController(MessageService messageService, SimpMessagingTemplate template) {
        this.messageService = messageService;
        this.template = template;
    }

    @PostMapping
    public ResponseEntity<EvaluateScore> nguoiDungDanhGia(@RequestBody EvaluateScoreDto evaluateScore) {
        return new ResponseEntity<>(messageService.nguoiDungDanhGia(evaluateScore), HttpStatus.OK);
    }

}
