package com.quinn.uploadingfiles;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: qiuyi
 * @Description:
 * @DateTime: 2023/1/6 11:11
 **/
@Configuration("storage")
public class StorageConfiguration {

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}
