package com.dmytr0.requestbin.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@PropertySource(value = {"classpath:application.properties", "classpath:build.properties"}, encoding = "UTF-8")
@ComponentScan("com.dmytr0.requestbin")
@EnableAsync
@EnableScheduling
public class AppConfig {

    @Value("${core.pool.size}")
    private int corePoolSize;

    @Value("${max.pool.size}")
    private int maxPoolSize;

    @Value("${queue.capacity}")
    private int queueCapacity;



    @Bean("task-executor")
    public Executor getTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("task-executor");
        executor.initialize();
        return executor;
    }

}
