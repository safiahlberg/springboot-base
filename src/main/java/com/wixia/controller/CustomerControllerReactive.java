package com.wixia.controller;

import com.wixia.domain.Customer;

import com.wixia.model.CustomerRepresentationModelAssembler;
import com.wixia.service.CustomerService;

import com.wixia.service.CustomerServiceReactive;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController(value = "/reactive/customers")
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
