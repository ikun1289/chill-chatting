package com.ttnm.chillchatting.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/6/22
 * Time      : 13:46
 * Filename  : EnumXepLoaiDeTai
 */
@Getter
@AllArgsConstructor
public enum EnumChannel {

    CHANNEL_1("CHANNEL_1","channel 1"),
    CHANNEL_2("CHANNEL_2","channel 2"),
    CHANNEL_3("CHANNEL_3","channel 3");

    private final String key;

    private final String value;
}
