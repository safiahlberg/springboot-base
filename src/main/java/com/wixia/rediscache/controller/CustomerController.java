package com.wixia.rediscache.controller;

import com.wixia.rediscache.persistence.CustomerEo;
import com.wixia.rediscache.service.CustomerService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public List<CustomerEo> findAll() {
        return customerService.findAll();
    }

    /**
     * This is a bit convoluted, it requires exposing the ID's from persistence, which we don't do in our system,
     * but OK for POC.
     *
     * @param id The customer ID (database ID in this case, but it could possibly be reworked to be a data ID instead)
     * @return the customer corresponding to the ID
     */
    @GetMapping("/customers/findById")
    public CustomerEo findById(@RequestParam long id) {
        return customerService.findById(id).orElse(null);
    }

    @GetMapping("/customers/findByFirstName")
    public List<CustomerEo> findByFirstName(@RequestParam String firstName) {
        return customerService.findByFirstName(firstName);
    }

    @GetMapping("/customers/findByLastName")
    public List<CustomerEo> findByLastName(@RequestParam String lastName) {
        return customerService.findByLastName(lastName);
    }

    @GetMapping("/customers/findByFirstNameAndLastName")
    public CustomerEo findByFirstNameAndLastName(@RequestParam String firstName,
                                                 @RequestParam String lastName) {
        return customerService.findByFirstNameAndLastName(firstName, lastName);
    }
}
