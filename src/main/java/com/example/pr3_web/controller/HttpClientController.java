package com.example.pr3_web.controller;

import com.example.pr3_web.service.PlaceHolderClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * here is my controller
 */
@RestController
@RequestMapping("/api")
public class HttpClientController {
    private final PlaceHolderClient client;

    public HttpClientController(PlaceHolderClient client) {
        this.client = client;
    }

    @GetMapping("/getMethod")
    public String getPosts() throws IOException, InterruptedException {
        return client.getPosts();
    }

    @GetMapping("/post")
    public String postMethod() throws IOException, InterruptedException {
        return client.postMethod();
    }

    @GetMapping("/head")
    public Map<String,String> headRequest() throws IOException, InterruptedException {
        return client.sendHeadRequest();
    }

    @GetMapping("/options")
    public Map<String,String> optionsRequest() throws IOException, InterruptedException {
        return client.sendOptionsRequest();
    }
}
