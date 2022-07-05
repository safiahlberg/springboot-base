package com.wixia.rediscache.persistence;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DatabaseExampleDataLoader {

    @Bean
    CommandLineRunner init(CustomerRepository customerRepository, ItemRepository itemRepository) {
        return args -> {
            customerRepository.save(new CustomerEo("Frodo", "Baggins"));
            customerRepository.save(new CustomerEo("Bilbo", "Baggins"));
            customerRepository.save(new CustomerEo("Peregrin", "Took"));
            customerRepository.save(new CustomerEo("Meriadoc", "Brandybuck"));
            customerRepository.save(new CustomerEo("Samwise", "Gamgee"));

            itemRepository.save(new ItemEo("Lembas", 1));
            itemRepository.save(new ItemEo("Mithril Shirt", 10000000));
            itemRepository.save(new ItemEo("The One Ring", 1000000000));

        };
    }
}
