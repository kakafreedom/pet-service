package com.wimonrat.pet.exception;

import com.wimonrat.pet.errorcode.ResponseCode;

public class InvalidEnumValueException extends BaseException {

    public InvalidEnumValueException(ResponseCode responseCode, String message) {
        super(responseCode, message);
    }

}
