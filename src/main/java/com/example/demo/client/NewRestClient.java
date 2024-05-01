package com.example.demo.client;

import com.example.demo.handlers.ApiErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Objects;

/**
 * @author Junaid Shakeel
 */
@Component
@Slf4j
public class NewRestClient {
    @Value("${http.client.ssl.trust-store}")
    private Resource store;
    @Value("${http.client.ssl.trust-store-password}")
    private String keyStorePassword;

    public <R, T> R invokeRestApi(String url, HttpMethod httpMethod, MultiValueMap<String, String> headerParams, T requestBody, Class<R> responseDto, boolean isSSLRequired){
        try {

            return RestClient.builder()
                    .requestFactory(clientHttpRequestFactory(isSSLRequired))
                    .build()
                    .method(httpMethod)
                    .uri(url)
                    .headers(headers -> headers.addAll(headerParams))
                    .body(Objects.isNull(requestBody) ?  new Object() : requestBody)
                    .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, new ApiErrorHandler())
                    .onStatus(HttpStatusCode::is5xxServerError, new ApiErrorHandler())
                    .body(responseDto);
        }
        catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException |
               IOException e){

            log.error("exception occurred {}",e.getMessage());
        }

        return null;
    }

    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory(boolean isSSLRequired) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        if (isSSLRequired) {
            final SSLContext sslContext2 = SSLContexts.custom().loadTrustMaterial(store.getURL(), keyStorePassword.toCharArray()).build();
            final SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext2, NoopHostnameVerifier.INSTANCE);
            final Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("https", sslConnectionSocketFactory)
                    .register("http", new PlainConnectionSocketFactory()).build();
            final BasicHttpClientConnectionManager connectionManager2 = new BasicHttpClientConnectionManager(socketFactoryRegistry);
            final CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager2).build();
            return new HttpComponentsClientHttpRequestFactory(httpClient);
        }
        return new HttpComponentsClientHttpRequestFactory(HttpClients.createDefault());
    }
}
