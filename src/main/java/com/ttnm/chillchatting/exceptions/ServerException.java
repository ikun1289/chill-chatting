package com.ttnm.chillchatting.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/30/22
 * Time      : 10:18
 * Filename  : ServerException
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ServerException extends RuntimeException {
    public ServerException() {
    }

    public ServerException(String message) {
        super(message);
    }
}
