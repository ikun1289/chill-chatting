package com.ttnm.chillchatting.dtos.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

    private String message;

    private String token;

    private String guestName;

    private String channel; //check enum
}
