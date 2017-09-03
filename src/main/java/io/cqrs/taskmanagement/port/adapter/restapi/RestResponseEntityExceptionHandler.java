package io.cqrs.taskmanagement.port.adapter.restapi;

import io.cqrs.taskmanagement.domain.model.DomainException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String RESOURCE_NOT_FOUND = "Resource not found";
    private static final String DOMAIN_VALIDATION_ERROR = "Domain Validation Error";

    private static final Logger logger = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @ExceptionHandler(value = {EntityNotFoundException.class})
    protected ResponseEntity<Object> handleEntityNotFoundException(RuntimeException ex, WebRequest request) {
        logger.info(RESOURCE_NOT_FOUND, ex);

        return handleExceptionInternal(ex, RESOURCE_NOT_FOUND, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {DomainException.class})
    protected ResponseEntity<Object> handleDomainException(RuntimeException ex, WebRequest request) {
        logger.error(DOMAIN_VALIDATION_ERROR, ex);

        return handleExceptionInternal(ex, DOMAIN_VALIDATION_ERROR, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

}