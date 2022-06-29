package com.wixia.rediscache.resource;

import com.wixia.rediscache.persistence.CustomerEo;
import com.wixia.rediscache.persistence.CustomerRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @RequestMapping("/customers")
    public ResponseEntity<CollectionModel<EntityModel<CustomerEo>>> findAll() {

        final List<EntityModel<CustomerEo>> customers = StreamSupport.stream(
                customerRepository.findAllSlow().spliterator(), false)
                .map(customer -> EntityModel.of(customer,
                        linkTo(methodOn(CustomerController.class).findOne(customer.getId())).withSelfRel(),
                        linkTo(methodOn(CustomerController.class).findAll()).withRel("customers")))
                        .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(customers,
                        linkTo(methodOn(CustomerController.class).findAll()).withSelfRel())
        );
    }

    /**
     * This is a bit convoluted, it requires exposing the ID's from persistence, which we don't do
     *
     * @param id
     * @return
     */
    @RequestMapping("/customers/{id}")
    public ResponseEntity<EntityModel<CustomerEo>> findOne(@PathVariable long id) {
        return customerRepository.findById(id)
                .map(customer -> EntityModel.of(customer,
                        linkTo(methodOn(CustomerController.class).findOne(customer.getId())).withSelfRel(),
                        linkTo(methodOn(CustomerController.class).findAll()).withRel("customers")))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
