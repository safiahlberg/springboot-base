package com.wixia.domain;

import com.wixia.controller.CustomerControllerReactive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CustomerRepositoryReactive {

    private static final Logger log = LoggerFactory.getLogger(
        CustomerRepositoryReactive.class);

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
        log.info("Costly call to database or external system. CustomerRepositoryReactive.findById({})", id);
        return Mono.just(CUSTOMER_DATA.get(id)).log();
    }

    public Flux<Customer> findAll() {
        log.info("Costly call to database or external system. CustomerRepositoryReactive.findAll()");
        return Flux.fromIterable(CUSTOMER_DATA.values()).log();
    }

    public Mono<Customer> save(Customer customer) {
        log.info("Costly call to database or external system. CustomerRepositoryReactive.save({})", customer);
        Customer existingCustomer = CUSTOMER_DATA.get(customer.getId());
        if (existingCustomer != null) {
            existingCustomer.setFirstName(customer.getFirstName());
            existingCustomer.setLastName(customer.getLastName());
            existingCustomer.setItems(customer.getItems());
        }
        return Mono.just(existingCustomer).log();
    }

}
