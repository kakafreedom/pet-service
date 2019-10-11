package com.wimonrat.pet.exception.mapper;


import com.google.common.base.Strings;
import com.wimonrat.pet.exception.BaseException;
import com.wimonrat.pet.exception.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CustomExceptionMapper implements ExceptionMapper<BaseException> {
    private final Logger logger = LoggerFactory.getLogger(CustomExceptionMapper.class);

    @Override
    public Response toResponse(BaseException exception) {
        logger.debug("Error Response : ", exception);
        if (Strings.isNullOrEmpty(exception.getMessage())) {
            return Response.status(exception.getErrorCode().getHttpStatus())
                    .entity(exception.getErrorCode())
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .build();
        }else {
            return Response.status(exception.getErrorCode().getHttpStatus())
                    .entity(new Error(exception.getErrorCode().getErrorCode(), exception.getMessage()))
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .build();
        }

    }
}