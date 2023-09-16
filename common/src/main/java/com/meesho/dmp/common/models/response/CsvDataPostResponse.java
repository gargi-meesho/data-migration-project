package com.meesho.dmp.common.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CsvDataPostResponse {
    private boolean success;
    private HttpStatus status;
    private String message;
    private ZonedDateTime created_at;
}
