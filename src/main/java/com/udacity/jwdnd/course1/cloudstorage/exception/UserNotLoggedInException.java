package com.udacity.jwdnd.course1.cloudstorage.exception;

public class UserNotLoggedInException extends RuntimeException {
    public UserNotLoggedInException(String message) {
        super(message);
    }

    public UserNotLoggedInException(String message, Throwable cause) {
        super(message, cause);
    }
}
