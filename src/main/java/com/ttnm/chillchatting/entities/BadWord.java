package com.ttnm.chillchatting.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection="bad-word")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BadWord {

    @Id
    private String id;

    private String badWord;
}
