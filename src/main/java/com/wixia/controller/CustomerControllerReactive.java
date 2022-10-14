package com.wixia.controller;

import com.wixia.domain.Customer;
import com.wixia.service.CustomerServiceReactive;

import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/reactive/customers")
public class CustomerControllerReactive {

    private final CustomerServiceReactive service;

    private final ReactiveRedisOperations<String, Customer> customerOps;

    private final ReactiveRedisConnectionFactory factory;

    public CustomerControllerReactive(
        CustomerServiceReactive customerService, ReactiveRedisOperations<String, Customer> customerOps, ReactiveRedisConnectionFactory factory) {
        this.service = customerService;
        this.customerOps = customerOps;
        this.factory = factory;
    }

    // GET

    @GetMapping
    public Flux<Customer> getAll() {
        return customerOps.keys("*")
            .flatMap(customerOps.opsForValue()::get)
            .switchIfEmpty(
                findAllAndPersistToRedis()
            );
    }

    @GetMapping("/{id}")
    public Mono<Customer> getOne(@PathVariable long id) {
        return this.service.findById(id);
    }

    private Flux<Customer> findAllAndPersistToRedis() {
        return factory
            .getReactiveConnection()
            .serverCommands()
            .flushAll()
            .thenMany(service.findAll()).flatMap(
                customer -> customerOps.opsForValue().set(
                    customer.getId().toString(), customer))
            .thenMany(
                customerOps.keys("*")
                    .flatMap(customerOps.opsForValue()::get)
            );
    }
}
