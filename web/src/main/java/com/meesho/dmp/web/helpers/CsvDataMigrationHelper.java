package com.meesho.dmp.web.helpers;

import com.meesho.dmp.common.models.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.persistence.PersistenceException;
import javax.validation.ValidationException;

import static com.meesho.dmp.common.utils.CommonUtils.createApiResponse;

@Slf4j
@Component
public class CsvDataMigrationHelper {

    public ResponseEntity<ApiResponse<Void>> handleExceptions(Exception e) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String errorMessage = "Internal Server Error";

        if (e instanceof ValidationException) {
            status = HttpStatus.BAD_REQUEST;
            errorMessage = "Validation Error";
        } else if (e instanceof PersistenceException) {
            errorMessage = "Database Error";
        }

        log.error("{}: {}", errorMessage, ExceptionUtils.getStackTrace(e));

        return createApiResponse(false, status, errorMessage + ": " + e.getMessage());
    }

}
