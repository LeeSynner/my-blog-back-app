package ru.yandex.practicum.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.Validator;

@Configuration
public class ValidationConfig {

    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }
}