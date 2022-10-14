package com.wixia.controller;

import com.wixia.domain.Customer;
import com.wixia.service.CustomerServiceReactive;

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

    public CustomerControllerReactive(
        CustomerServiceReactive customerService) {
        this.service = customerService;
    }

    // GET

    @GetMapping
    public Flux<Customer> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Customer> getOne(@PathVariable long id) {
        return this.service.findById(id);
    }

}
