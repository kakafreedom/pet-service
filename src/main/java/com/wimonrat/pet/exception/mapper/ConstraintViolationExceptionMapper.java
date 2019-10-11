package com.wimonrat.pet.exception.mapper;

import com.wimonrat.pet.exception.ValidationErrors;
import org.hibernate.validator.internal.engine.path.PathImpl;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException cve) {
        ValidationErrors validationErrors = new ValidationErrors();
        for (ConstraintViolation<?> c : cve.getConstraintViolations()) {
            String field = ((PathImpl) c.getPropertyPath()).getLeafNode().getName();
            if (field == null) {
                field = c.getLeafBean().getClass().getSimpleName();
            }
            validationErrors.add(field, c.getMessage());
        }
        return Response.status(400).entity(validationErrors).build();
    }
}