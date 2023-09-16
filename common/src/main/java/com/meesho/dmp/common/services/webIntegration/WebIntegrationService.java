package com.meesho.dmp.common.services.webIntegration;

import com.meesho.dmp.common.models.request.CsvDataPostRequest;
import com.meesho.dmp.common.models.response.CsvDataPostResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface WebIntegrationService {
    ResponseEntity<CsvDataPostResponse> sendCsvPostRequest(CsvDataPostRequest requestBody);
}
