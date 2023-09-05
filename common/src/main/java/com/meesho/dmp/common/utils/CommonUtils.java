package com.meesho.dmp.common.utils;

import com.meesho.dmp.common.models.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CommonUtils {

    public static <T> ResponseEntity<ApiResponse<T>> createApiResponse(boolean success, HttpStatus status,
                                                                       String message) {
        return createApiResponse(success, status, message, null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> createApiResponse(boolean success, HttpStatus status,
                                                                       String message,
                                                                       T data) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                                             .success(success)
                                             .status(status)
                                             .message(message)
                                             .data(data)
                                             .build();

        return ResponseEntity.status(response.getStatus())
                             .body(response);
    }

}
