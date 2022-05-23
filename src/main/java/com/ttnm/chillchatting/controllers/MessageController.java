package com.ttnm.chillchatting.controllers;


import com.ttnm.chillchatting.dtos.MyEnum;
import com.ttnm.chillchatting.dtos.RegistNameRequest;
import com.ttnm.chillchatting.dtos.RegistNameResponse;
import com.ttnm.chillchatting.dtos.message.MessageDto;
import com.ttnm.chillchatting.entities.Message;
import com.ttnm.chillchatting.exceptions.InvalidException;
import com.ttnm.chillchatting.services.message.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Random;

@EnableScheduling
@RestController
@RequestMapping("/rest/message")
public class MessageController {

    private final MessageService messageService;

    private final SimpMessagingTemplate template;

    public MessageController(MessageService messageService, SimpMessagingTemplate template) {
        this.messageService = messageService;
        this.template = template;
    }

    @GetMapping("/kenh")
    public ResponseEntity<List<MyEnum>> getKenhs() {
        return new ResponseEntity<>(messageService.getKenhs(), HttpStatus.OK);
    }

    @PostMapping("/dk-ten")
    public ResponseEntity<RegistNameResponse> dangKyTenChat(@RequestBody RegistNameRequest dto) throws Exception {
        return new ResponseEntity<>(messageService.dangKyTenChat(dto), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Message> guiMessage(@RequestBody MessageDto dto) throws Exception {
        Message message = messageService.guiMessage(dto);
        System.out.println(message.getChannel());
        this.template.convertAndSend("/message/" + message.getChannel(), message);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/{kenh}")
    public ResponseEntity<List<Message>> getListOfLatestMessages(@PathVariable String kenh) {
        return new ResponseEntity<>(messageService.getTheLatestMessages(kenh), HttpStatus.OK);
    }

    public void scheduledUpdateMessage() throws InterruptedException {
        for (MyEnum kenh : messageService.getKenhs()) {
//            MessageDto message = new MessageDto();
//            message.setChannel(kenh.getKey());
//            message.setMessage(new Random().nextInt()+"");
//            message.setGuestName("anonymous#1");
//            messageService.guiMessage(message);
            this.template.convertAndSend("/message/" + kenh.getKey(), messageService.getTheLatestMessages(kenh.getKey()));
        }
    }

}
