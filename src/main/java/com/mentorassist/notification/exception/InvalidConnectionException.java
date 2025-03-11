package com.mentorassist.notification.exception;

public class InvalidConnectionException extends RuntimeException {

    public InvalidConnectionException(String callerId, String connectionId, Throwable cause) {
        super(String.format("Could not send message to %s with connectionId %s", callerId, connectionId), cause);
    }

}
