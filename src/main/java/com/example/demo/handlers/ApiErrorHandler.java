package com.example.demo.handlers;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.io.IOException;

public class ApiErrorHandler implements RestClient.ResponseSpec.ErrorHandler {
    @Override
    public void handle(HttpRequest request, ClientHttpResponse response) throws RestClientException, IOException {

        var errorResponse = new String(response.getBody().readAllBytes());
        throw new RestClientException(String.format("api error occurred %s with status code%s",errorResponse, response.getStatusCode()));
    }
}
