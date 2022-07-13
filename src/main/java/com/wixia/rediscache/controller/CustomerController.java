package com.wixia.rediscache.controller;

import com.wixia.rediscache.persistence.CustomerEo;
import com.wixia.rediscache.service.CustomerService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public List<CustomerEo> findAll(@RequestParam(defaultValue = "0") long delayInMs) {
        return customerService.findAllDelayable(delayInMs);
    }

    /**
     * This is a bit convoluted, it requires exposing the ID's from persistence, which we don't do in our system,
     * but OK for POC.
     *
     * @param id The customer ID (database ID in this case, but it could possibly be reworked to be a data ID instead)
     * @return the customer corresponding to the ID
     */
    @GetMapping("/customers/{id}")
    public CustomerEo findOne(@PathVariable long id) {
        return customerService.findById(id).orElse(null);
    }

}
