# spring-boot-new-rest-client
Spring boot 3.2 introduced new Rest Client for blocking calls. It's an updated version of rest template similar to a non-blocking web client but it's for blocking API calls. 

I'm using Java 17 and Spring Boot version 3.2.5. 

Implementation is generic you can call any API with any HTTP method. If the body is not needed then pass an empty String.

Sample:

GET Api call:
   ```sh
     @Autowired
     private NewRestClient newRestClient;

     public  void invokeGetApi(){
        //add your custom headers based on logic
        HttpHeaders httpHeaders= new HttpHeaders();
        var response= newRestClient.invokeRestApi("https://jsonplaceholder.typicode.com/posts", HttpMethod.GET,httpHeaders,"",String.class,Boolean.FALSE);
        log.info("api response {}",response);

    }  
   ```
POST Api call:
   ```sh
     @Autowired
     private NewRestClient newRestClient;

     public void invokePostApi(){
       // sample body
       var body =  Map.of("title","test post request","body","test body","userId","123");
        //add your custom headers based on logic
        HttpHeaders httpHeaders= new HttpHeaders();
        var response= newRestClient.invokeRestApi("https://jsonplaceholder.typicode.com/posts", HttpMethod.POST,httpHeaders,body,String.class,Boolean.FALSE);
        log.info("api response {}",response);
    }
   ```
