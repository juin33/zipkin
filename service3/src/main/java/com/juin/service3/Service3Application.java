package com.juin.service3;

import com.juin.client.ZipkinClientConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @author kejun
 * @date 2019/1/16 下午6:29
 */
@SpringBootApplication
@Import(ZipkinClientConfiguration.class)
public class Service3Application {
    public static void main(String[] args) {
        SpringApplication.run(Service3Application.class, args);
    }
}
