package ru.otus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.services.IOService;

@Configuration
public class AppConfig {
    @Bean
    IOService ioService() {
        return new IOService(System.in, System.out);
    }
}
