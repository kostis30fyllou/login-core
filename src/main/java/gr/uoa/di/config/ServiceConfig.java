package gr.uoa.di.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class ServiceConfig {


    @Bean
    public Random random() {
        return new Random();
    }
}
