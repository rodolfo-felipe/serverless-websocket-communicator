package com.mentorassist.notification.exception;

public class MissingParameterException extends RuntimeException {

    public MissingParameterException(String param) {
        super(String.format("Required request parameter '%s'!", param));
    }

}
