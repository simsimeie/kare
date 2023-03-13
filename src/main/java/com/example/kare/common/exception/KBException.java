package com.example.kare.common.exception;

import com.example.kare.common.constant.ErrorCode;
import lombok.Getter;

@Getter
public class KBException extends RuntimeException{

    private final ErrorCode errorCode;

    public KBException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public KBException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public KBException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public KBException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public KBException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }
}
