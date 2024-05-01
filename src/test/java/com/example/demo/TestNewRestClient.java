package com.example.demo;

import com.example.demo.client.NewRestClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.Map;

@Slf4j
@SpringBootTest
public class TestNewRestClient {
    @Autowired
    private NewRestClient newRestClient;



    @Test
    void invokeGetApi(){

        //add your custom headers based on logic
        HttpHeaders httpHeaders= new HttpHeaders();
        var response= newRestClient.invokeRestApi("https://jsonplaceholder.typicode.com/posts", HttpMethod.GET,httpHeaders,"",String.class,Boolean.FALSE);
         log.info("api response {}",response);

    }

    @Test
    void invokePostApi(){
       // sample body
       var body =  Map.of("title","test post request","body","test body","userId","123");
        //add your custom headers based on logic
        HttpHeaders httpHeaders= new HttpHeaders();
        var response= newRestClient.invokeRestApi("https://jsonplaceholder.typicode.com/posts", HttpMethod.POST,httpHeaders,body,String.class,Boolean.FALSE);
        log.info("api response {}",response);

    }
}
