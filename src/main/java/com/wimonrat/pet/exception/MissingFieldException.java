package com.wimonrat.pet.exception;

import com.wimonrat.pet.errorcode.ResponseCode;

public class MissingFieldException extends BaseException {

    public MissingFieldException(ResponseCode responseCode, String message) {
        super(responseCode, message);
    }

}
