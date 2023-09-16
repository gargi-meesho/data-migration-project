package com.meesho.dmp.common.exceptions;

import com.meesho.dmp.common.models.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.meesho.dmp.common.utils.CommonUtils.createErrorResponse;

@RestControllerAdvice
@Slf4j
public class RestApiExceptionHandler {

    private static final String LOG_PREFIX = "[RestApiExceptionHandler]";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception exception, WebRequest request) {
        log.info("Request: {}", request);

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "Internal server error";

        if (exception instanceof ValidationException) {
            status = HttpStatus.BAD_REQUEST;
            message = "Validation error";

        } else if (exception instanceof DataAccessException | exception instanceof JDBCConnectionException) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
            message = "Database error";

        } else if (exception instanceof NoSuchElementException) {
            status = HttpStatus.NOT_FOUND;
            message = "Resource not found";

        } else if (exception instanceof HttpMessageNotReadableException
                || exception instanceof IllegalArgumentException) {
            status = HttpStatus.BAD_REQUEST;
            message = "Invalid request";

        }
        message = String.format("%s: %s", message, exception.getMessage());

        log.error("{} {}: {}", LOG_PREFIX, message, ExceptionUtils.getStackTrace(exception));
        return createErrorResponse(status, message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException exception) {

        Map<String, Object> errors = new HashMap<>();
        if (!exception.getConstraintViolations().isEmpty()) {
            for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
                String fieldName = null;
                for (Path.Node node : constraintViolation.getPropertyPath()) {
                    fieldName = node.getName();
                }
                errors.put(fieldName, constraintViolation.getMessage());
            }
        }
        String message = "Constraint violation error: " + errors;
        log.error("{} {}: {}", LOG_PREFIX, message, ExceptionUtils.getStackTrace(exception));

        return createErrorResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(NoRecordFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoRecordFoundException(
            NoRecordFoundException exception) {

        String message = exception.getMessage();
        log.error("{} {}: {}", LOG_PREFIX, message, ExceptionUtils.getStackTrace(exception));

        return createErrorResponse(HttpStatus.BAD_REQUEST, message);
    }
}
