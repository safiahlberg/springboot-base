package com.wixia.rediscache.persistence;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DatabaseExampleDataLoader {

    @Bean
    CommandLineRunner init(CustomerRepository repository) {
        return args -> {
            repository.save(new CustomerEo("Frodo", "Baggins"));
            repository.save(new CustomerEo("Bilbo", "Baggins"));
        };
    }
}
