package com.wixia.controller;

import com.wixia.domain.Customer;
import com.wixia.service.CustomerServiceReactive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${redis.ttl.minutes:10}")
    private int redisDataTTL;

    public CustomerControllerReactive(CustomerServiceReactive customerService,
                                      ReactiveRedisOperations<String, Customer> customerOps) {
        this.service = customerService;
        this.customerOps = customerOps;
    }

    // GET

    @GetMapping
    public Flux<Customer> getAll() {
        log.info("CustmerControllerReactive.getAll()");
        return redisGetAll()
            .switchIfEmpty(
                // Flux.defer(..) is needed because otherwise a hot Publisher will be created
                Flux.defer(this::findAllAndPersistToRedis)
            )
            .onErrorResume(
                throwable -> service.findAll()
            );
    }

    @GetMapping("/{id}")
    public Mono<Customer> getOne(@PathVariable long id) {
        log.info("CustomerControllerReactive.getOne({})", id);
        return redisGetOne(id)
            .switchIfEmpty(Mono.defer(() -> findOneAndPersistToRedis(id))) // https://stackoverflow.com/questions/54373920/mono-switchifempty-is-always-called
            .onErrorResume(throwable -> service.findById(id));
    }

    private Flux<Customer> findAllAndPersistToRedis() {
        log.info("CustmerControllerReactive.findAllAndPersistToRedis()");
        return service.findAll()
            .flatMap(this::redisSet)
            .thenMany(redisGetAll());
    }

    private Mono<Customer> findOneAndPersistToRedis(long id) {
        log.info("CustomerControllerReactive.findOneAndPersistToRedis({})", id);
        return service.findById(id)
            .flatMap(this::redisSet) // Trial and error brought this. A map was tried first, since the intuition around Mono suggested that, but a flatMap is needed
            .then(redisGetOne(id));
    }

    private Flux<Customer> redisGetAll() {
        return customerOps.keys("*")
            .flatMap(customerOps.opsForValue()::get);
    }

    private Mono<Customer> redisGetOne(long id) {
        return customerOps.opsForValue().get(Long.toString(id));
    }

    private Mono<Boolean> redisSet(Customer customer) {
        log.info("set to redis");
        return customerOps.opsForValue().set(
            customer.getId().toString(), customer,
            getTimeout()).log();
    }

    private Duration getTimeout() {
        return Duration.ofMinutes(redisDataTTL);
    }

}
