package com.ttnm.chillchatting.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection="message")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Message {

    @Id
    private String id;

    private String message;

    private String channel;

    private Date createdDate;

    private String guestName;

    private boolean enable = true;
}
