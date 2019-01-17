package com.juin.service2;

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
public class Service2Controller {
    private RestTemplate restTemplate;

    @Autowired
    Service2Controller(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @GetMapping(value = "/service2")
    public String getService() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "service2 sleep 100ms ->"+ restTemplate.getForObject("http://localhost:8083/service3",String.class);
    }
}
