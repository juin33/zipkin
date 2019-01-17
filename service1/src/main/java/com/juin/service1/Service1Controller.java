package com.juin.service1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author kejun
 * @date 2019/1/17 上午10:18
 */
@EnableAutoConfiguration
@RestController
public class Service1Controller {
    private RestTemplate restTemplate;

    @Autowired
    Service1Controller(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @GetMapping(value = "/service1")
    public String getService() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "service1 sleep 100ms ->" + restTemplate.getForObject("http://localhost:8082/service2",String.class);
    }
}
