package com.juin.service3;

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
public class Service3Controller {
    private RestTemplate restTemplate;

    @Autowired
    Service3Controller(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @GetMapping(value = "/service3")
    public String getService() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "service3 sleep 100ms ->"+"finish";
    }
}
