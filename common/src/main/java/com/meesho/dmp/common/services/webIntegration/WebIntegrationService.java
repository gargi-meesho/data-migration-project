package com.meesho.dmp.common.services.webIntegration;

import com.meesho.dmp.common.models.ApiResponse;
import com.meesho.dmp.common.models.CsvDataPostRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.meesho.dmp.common.constants.ApiConstants.BASE_API_URL;
import static com.meesho.dmp.common.constants.ApiConstants.SAVE_PRICING_DATA_ENDPOINT;

@Service
public class WebIntegrationService {

    private final RestTemplate restTemplate;

    public WebIntegrationService() {
        this.restTemplate = new RestTemplate();
    }

    private HttpHeaders createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }

    public ResponseEntity<ApiResponse> sendCsvPostRequest(CsvDataPostRequest requestBody) {
        HttpHeaders httpHeaders = createHttpHeaders();
        String apiUrl = BASE_API_URL + "/" + SAVE_PRICING_DATA_ENDPOINT;
        HttpEntity<CsvDataPostRequest> requestEntity = new HttpEntity<>(requestBody, httpHeaders);

        return restTemplate.postForEntity(apiUrl, requestEntity, ApiResponse.class);
    }
}
