package com.wimonrat.pet.exception;

import com.wimonrat.pet.errorcode.ResponseCode;

public class PetNotFoundException extends BaseException {
    public PetNotFoundException() {
        super(ResponseCode.PET_NOT_FOUND);
    }
}
