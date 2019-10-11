package com.wimonrat.pet.exception.mapper;

import com.google.common.base.Strings;
import com.wimonrat.pet.errorcode.ResponseCode;
import com.wimonrat.pet.exception.InvalidEnumValueException;
import com.wimonrat.pet.exception.Error;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        String message = exception.getMessage();
        if (exception.getCause() != null && !Strings.isNullOrEmpty(exception.getCause().getMessage())) {
            message = exception.getCause().getMessage();
        }
        Error error = new Error(ResponseCode.GENERIC_EXEPTION.getErrorCode(), message);
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        if(exception instanceof InvalidEnumValueException ||
                exception.getCause() != null && exception.getCause() instanceof InvalidEnumValueException){
            status = Response.Status.BAD_REQUEST;
            InvalidEnumValueException ex = (InvalidEnumValueException)exception.getCause();
            error = new Error(ex.getErrorCode().getErrorCode(), ex.getMessage());
        } return Response
                .status(status)
                .entity(error)
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

}