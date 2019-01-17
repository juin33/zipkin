package com.juin.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

/**
 * @author kejun
 * @date 2019/1/16 下午6:29
 */
@SpringBootApplication
@ImportResource("spring/appContext.xml")
@Import(ZipkinClientConfiguration.class)
public class ClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }
}
