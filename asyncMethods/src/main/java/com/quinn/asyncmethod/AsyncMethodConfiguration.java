package com.quinn.asyncmethod;

import com.quinn.asyncmethod.ticketSold.Ticket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @Author: qiuyi
 * @Description:
 * @DateTime: 2023/2/9 11:02
 **/
@Configuration
public class AsyncMethodConfiguration {

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("AsyncMethod-");
        executor.initialize();
        return executor;
    }
}
