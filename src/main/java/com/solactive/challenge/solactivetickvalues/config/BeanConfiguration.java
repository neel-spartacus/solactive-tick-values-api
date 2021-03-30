package com.solactive.challenge.solactivetickvalues.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

import java.util.concurrent.Executors;

@Configuration
public class BeanConfiguration {

    @Bean
    @Qualifier("tickValueTaskExecutor")
    public TaskExecutor tickValueTaskExecutor() {
        return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(10));
    }


}
