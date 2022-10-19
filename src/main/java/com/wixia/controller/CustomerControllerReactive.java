package com.wixia.controller;

import com.wixia.domain.Customer;
import com.wixia.service.CustomerServiceReactive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping(value = "/reactive/customers")
public class CustomerControllerReactive {

    private static final Logger log = LoggerFactory.getLogger(
        CustomerControllerReactive.class);

    private final CustomerServiceReactive service;

    private final ReactiveRedisOperations<String, Customer> customerOps;

    private final ReactiveRedisConnectionFactory factory;

    @Value("${redis.ttl.minutes:10}")
    private int redisDataTTL;

    public CustomerControllerReactive(CustomerServiceReactive customerService,
                                      ReactiveRedisOperations<String, Customer> customerOps,
                                      ReactiveRedisConnectionFactory factory) {
        this.service = customerService;
        this.customerOps = customerOps;
        this.factory = factory;
    }

    // GET

    @GetMapping
    public Flux<Customer> getAll() {
        log.info("CustmerControllerReactive.getAll()");
        return customerOps.keys("*")
            .flatMap(customerOps.opsForValue()::get)
            .switchIfEmpty(
                // Flux.defer(..) is needed because otherwise a hot Publisher will be created
                Flux.defer(() -> findAllAndPersistToRedis())
            )
            .onErrorResume(
                throwable -> service.findAll()
            );
    }

    @GetMapping("/{id}")
    public Mono<Customer> getOne(@PathVariable long id) {
        log.info("CustomerControllerReactive.getOne({})", id);
        return customerOps.opsForValue().get(id)
            .switchIfEmpty(Mono.defer(() -> findOneAndPersistToRedis(id)))
            .onErrorResume(throwable -> service.findById(id));
    }

    private Flux<Customer> findAllAndPersistToRedis() {
        log.info("CustmerControllerReactive.findAllAndPersistToRedis()");
        // The following code is copied from https://spring.io/guides/gs/spring-data-reactive-redis/
        // But it may not be optimal for this case
        return factory
            .getReactiveConnection()
            .serverCommands()
            .flushAll()
            .thenMany(service.findAll()).flatMap(
                customer -> redisSet(customer))
            .thenMany(
                customerOps.keys("*")
                    .flatMap(customerOps.opsForValue()::get)
            );
    }

    private Mono<Customer> findOneAndPersistToRedis(long id) {
        log.info("CustomerControllerReactive.findOneAndPersistToRedis({})", id);
        return factory
            .getReactiveConnection()
            .serverCommands()
            .flushAll() // ?
            .then(service.findById(id)).map(
                customer -> redisSet(customer))
            .then(
                customerOps.opsForValue().get(id)
            );
    }

    private Duration getTimeout() {
        return Duration.ofMinutes(redisDataTTL);
    }

    private Mono<Boolean> redisSet(Customer customer) {
        return customerOps.opsForValue().set(
            customer.getId().toString(), customer,
            getTimeout());
    }

}
