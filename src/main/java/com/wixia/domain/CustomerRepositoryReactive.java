package com.wixia.domain;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CustomerRepositoryReactive {
    private static final Map<Long, Customer> CUSTOMER_DATA; // This is just to simulate data

    static {
        CUSTOMER_DATA = new HashMap<>();
        CUSTOMER_DATA.put(1L, new Customer(new Customer("Frodo", "Baggins"), 1L));
        CUSTOMER_DATA.put(2L, new Customer(new Customer("Bilbo", "Baggins"), 2L));
        CUSTOMER_DATA.put(3L, new Customer(new Customer("Samwise", "Gamgee"), 3L));
        CUSTOMER_DATA.put(4L, new Customer(new Customer("Peregrin", "Took"), 4L));
        CUSTOMER_DATA.put(5L, new Customer(new Customer("Meriadoc", "Brandybuck"), 5L));
    }

    public Mono<Customer> findById(long id) {
        return Mono.just(CUSTOMER_DATA.get(id)).log();
    }

    public Flux<Customer> findAll() {
        return Flux.fromIterable(CUSTOMER_DATA.values()).log();
    }

    public Mono<Customer> save(Customer Customer) {
        Customer existingCustomer = CUSTOMER_DATA.get(Customer.getId());
        if (existingCustomer != null) {
            existingCustomer.setFirstName(Customer.getFirstName());
            existingCustomer.setLastName(Customer.getLastName());
            existingCustomer.setItems(Customer.getItems());
        }
        return Mono.just(existingCustomer).log();
    }

}
