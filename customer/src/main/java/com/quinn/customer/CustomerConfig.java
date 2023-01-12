package com.quinn.customer;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * @Author: qiuyi
 * @Description:
 * @DateTime: 2022/12/27 11:25
 **/

@Configuration
public class CustomerConfig {

    @Bean
    public RestTemplate CreateRestTemplate(RestTemplateBuilder builder) {
        final RestTemplateBuilder restTemplateBuilder = builder
                .setConnectTimeout(Duration.ofMillis(3000))
                .setReadTimeout(Duration.ofMillis(3000));

        final RestTemplate restTemplate = restTemplateBuilder.build();

        return restTemplate;
    }
}
