package com.wixia.service;

import com.wixia.domain.Customer;
import com.wixia.domain.CustomerRepositoryReactive;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.wixia.configuration.CacheConfig.CUSTOMER_CACHE_REACTIVE;

@Service
public class CustomerServiceReactive {
    private final CustomerRepositoryReactive repository;

    public CustomerServiceReactive(CustomerRepositoryReactive repository) {
        this.repository = repository;
    }

    @Cacheable(value = CUSTOMER_CACHE_REACTIVE)
    public Mono<Customer> findById(long id) {
        return repository.findById(id);
    }

    @Cacheable(value = CUSTOMER_CACHE_REACTIVE)
    public Flux<Customer> findAll() {
        return repository.findAll();
    }

    public Mono<Customer> save(Customer Customer) {
        return repository.save(new Customer());
    }
}
