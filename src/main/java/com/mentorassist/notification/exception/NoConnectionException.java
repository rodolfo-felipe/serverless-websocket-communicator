package com.mentorassist.notification.exception;

public class NoConnectionException extends RuntimeException {

    public NoConnectionException(String callerId) {
        super(String.format("Could not find connection to caller %s", callerId));
    }

}
