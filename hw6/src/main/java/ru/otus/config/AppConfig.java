package ru.otus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.service.OutService;

@Configuration
public class AppConfig {
    @Bean
    OutService ioService() {
        return new OutService(System.out);
    }
}
