package com.wimonrat.pet.exception;

import com.wimonrat.pet.errorcode.ResponseCode;
import lombok.Getter;

@Getter
public class BaseException extends Exception {
    private final ResponseCode errorCode;

    public BaseException(final ResponseCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public BaseException(final ResponseCode errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }

}