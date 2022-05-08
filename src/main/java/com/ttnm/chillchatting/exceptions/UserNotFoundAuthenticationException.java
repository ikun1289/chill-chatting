package com.ttnm.chillchatting.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/30/22
 * Time      : 10:18
 * Filename  : UserNotFoundAuthenticationException
 */
public class UserNotFoundAuthenticationException extends AuthenticationException {
    public UserNotFoundAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public UserNotFoundAuthenticationException(String msg) {
        super(msg);
    }
}
