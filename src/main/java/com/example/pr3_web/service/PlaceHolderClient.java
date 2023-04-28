package com.example.pr3_web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.net.http.HttpClient.Version.HTTP_1_1;

@Service
public class PlaceHolderClient {
    public static final String API_URL = "https://jsonplaceholder.typicode.com/posts";
    Pattern URL_PATTERN =
            Pattern.compile("[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HTTP_1_1)
            .proxy(ProxySelector.of(new InetSocketAddress("185.253.45.177", 50100)))
            .build();

    private final String encoded = Base64.getEncoder()
            .encodeToString("danielmarandici2001:5Q3zRmxBbI".getBytes(StandardCharsets.UTF_8));


    public String getPosts() throws IOException, InterruptedException {
        validateUrl(API_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .version(HTTP_1_1)
                .header("accept", "application/json")
                .setHeader("Proxy-Authorization", "Basic " + encoded)
                .uri(URI.create(API_URL))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String postMethod() throws IOException, InterruptedException {
        var values = new HashMap<String, String>();
        values.put("userId", "24");
        values.put ("title", "my title");
        values.put("body", "my body");

        var objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writeValueAsString(values);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("accept", "application/json")
                .setHeader("Proxy-Authorization", "Basic " + encoded)
                .uri(URI.create(API_URL))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    // a HEAD request is a lightweight way of retrieving metadata about a resource
    // on the server without actually downloading the entire resource.
    public Map<String,String> sendHeadRequest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .header("accept", "application/json")
                .setHeader("Proxy-Authorization", "Basic " + encoded)
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .uri(URI.create(API_URL))
                .build();

        HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());

        System.out.println("Headers: ");
        HttpHeaders headers = response.headers();
        return mapHeaders(headers);
    }

    //n OPTIONS request is a type of HTTP request that is used to retrieve information about
    // the communication options available for a resource on a server.
    // It allows the client to determine which HTTP methods are supported by the server and other communication options.
    public Map<String, String> sendOptionsRequest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .header("accept", "application/json")
                .setHeader("Proxy-Authorization", "Basic " + encoded)
                .method("OPTIONS", HttpRequest.BodyPublishers.noBody())
                .uri(URI.create(API_URL))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        HttpHeaders optionHeaders = response.headers();
        return mapHeaders(optionHeaders);
    }

    private static Map<String, String> mapHeaders(HttpHeaders headers) {
        Map<String,String> resp = new java.util.HashMap<>(Collections.emptyMap());
        headers.map().forEach((key, value) -> resp.put(key,value.toString()));
        return resp;
    }

    private void validateUrl(String url){
        Matcher matcher = URL_PATTERN.matcher(url);
        if(!matcher.find()){
            throw new RuntimeException("invalid url");
        }
        System.out.println("url validated");
    }

}
