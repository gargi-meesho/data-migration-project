package com.meesho.dmp.common.services.webIntegration.implementation;

import com.meesho.dmp.common.models.request.CsvDataPostRequest;
import com.meesho.dmp.common.models.response.CsvDataPostResponse;
import com.meesho.dmp.common.services.webIntegration.WebIntegrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.meesho.dmp.common.constants.ApiConstants.BASE_API_URL;
import static com.meesho.dmp.common.constants.ApiConstants.SAVE_PRICING_DATA_ENDPOINT;

@Service
@Slf4j
public class WebIntegrationServiceImp implements WebIntegrationService {

    private final RestTemplate restTemplate;

    public WebIntegrationServiceImp() {
        this.restTemplate = new RestTemplate();
    }

    private HttpHeaders createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }

    public ResponseEntity<CsvDataPostResponse> sendCsvPostRequest(CsvDataPostRequest requestBody) {
        HttpHeaders httpHeaders = createHttpHeaders();
        String apiUrl = BASE_API_URL + SAVE_PRICING_DATA_ENDPOINT;
        HttpEntity<CsvDataPostRequest> requestEntity = new HttpEntity<>(requestBody, httpHeaders);

        return restTemplate.postForEntity(apiUrl, requestEntity, CsvDataPostResponse.class);
    }
}
