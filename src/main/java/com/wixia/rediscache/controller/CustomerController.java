package com.wixia.rediscache.controller;

import com.wixia.rediscache.persistence.CustomerEo;
import com.wixia.rediscache.persistence.CustomerRepository;
import com.wixia.rediscache.service.CustomerService;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * A tight coupling to between controller and repository, but OK for demonstration.
 */
@RestController
public class CustomerController {

    private final CustomerService customerService;
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public ResponseEntity<CollectionModel<EntityModel<CustomerEo>>> findAll(
            @RequestParam(defaultValue = "0") long delayInMs
    ) {

        final List<EntityModel<CustomerEo>> customers = StreamSupport.stream(
                customerService.findAllDelayable(delayInMs).spliterator(), false)
                .map(customer -> EntityModel.of(customer,
                        linkTo(methodOn(CustomerController.class).findOne(customer.getId())).withSelfRel(),
                        linkTo(methodOn(CustomerController.class).findAll(delayInMs)).withRel("customers")))
                        .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(customers,
                        linkTo(methodOn(CustomerController.class).findAll(delayInMs)).withSelfRel())
        );
    }

    /**
     * This is a bit convoluted, it requires exposing the ID's from persistence, which we don't do in our system,
     * but OK for POC.
     *
     * @param id The customer ID (database ID in this case, but it could possibly be reworked to be a data ID instead)
     * @return the customer corresponding to the ID
     */
    @GetMapping("/customers/{id}")
    public ResponseEntity<EntityModel<CustomerEo>> findOne(@PathVariable long id) {
        return customerService.findById(id)
                .map(customer -> EntityModel.of(customer,
                        linkTo(methodOn(CustomerController.class).findOne(customer.getId())).withSelfRel(),
                        linkTo(methodOn(CustomerController.class).findAll(0)).withRel("customers")))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
