package com.wixia.rediscache.service;

import com.wixia.rediscache.persistence.CustomerEo;
import com.wixia.rediscache.persistence.CustomerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CustomerService {

    Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Optional<CustomerEo> findById(long id) {
        return customerRepository.findById(id);
    }

    public List<CustomerEo> findAll() {
        return StreamSupport.stream(customerRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());
    }

    @Cacheable(value = "customerCache")
    public List<CustomerEo> findByLastName(String lastName) {
        return customerRepository.findByLastName(lastName);
    }

    @Cacheable(value = "customerCache")
    public List<CustomerEo> findByFirstName(String firstName) {
        return customerRepository.findByFirstName(firstName);
    }

    @Cacheable(value = "customerCache", key = "#firstName.concat('-').concat(#lastName)")
    public CustomerEo findByFirstNameAndLastName(String firstName, String lastName) {
        return customerRepository.findByFirstNameAndLastName(firstName, lastName);
    }
}
