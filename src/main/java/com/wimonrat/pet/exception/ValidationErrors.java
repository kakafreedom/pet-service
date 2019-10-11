package com.wimonrat.pet.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wimonrat.pet.errorcode.ResponseCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ValidationErrors {

    @JsonProperty("error_code")
    private String errorCode = ResponseCode.INVALID_REQUEST.getErrorCode();

    private String message = ResponseCode.INVALID_REQUEST.getMessage();

    @JsonProperty("error_details")
    private List<ValidationObject> errors = new ArrayList<>();

    public void add(String field, String error){
        errors.add(new ValidationObject(field, error));
    }

}
