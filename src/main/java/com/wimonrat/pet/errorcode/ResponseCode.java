package com.wimonrat.pet.errorcode;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static org.eclipse.jetty.http.HttpStatus.*;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ResponseCode {
    PET_NOT_FOUND("PET_NOT_FOUND", NOT_FOUND_404, "Pet not found."),
    PET_ID_MISMATCHED("PET_ID_MISMATCHED", BAD_REQUEST_400, "Pet ID provided in the request are mismatched."),
    INVALID_REQUEST("INVALID_REQUEST", BAD_REQUEST_400, "Invalid request."),
    GENERIC_EXEPTION("GENERIC_EXCEPTION", INTERNAL_SERVER_ERROR_500, "Internal server error."),
    INVALID_PET_TYPE("INVALID_PET_TYPE", BAD_REQUEST_400, "Pet type provided in the request is not supported."),
    INVALID_SEX_TYPE("INVALID_SEX_TYPE", INTERNAL_SERVER_ERROR_500, "Invalid value for sex. Valid values are {F,M}."),
    MISSING_REQUIRED_FIELDS("MISSING_REQUIRED_FIELDS", BAD_REQUEST_400, "Missing required field(s).");


    @JsonProperty("error_code")
    private String errorCode;

    @JsonIgnore
    private int httpStatus;

    private String message;

}

