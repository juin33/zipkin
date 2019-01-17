package com.juin.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author kejun
 * @date 2019/1/16 下午6:36
 */
@EnableAutoConfiguration
@RestController
public class ClientController {
    private RestTemplate restTemplate;

    @Autowired
    ClientController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @GetMapping(value = "/index")
    public String getService() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "client sleep 200ms ->" + restTemplate.getForObject("http://localhost:8081/service1",String.class);
    }
}
