package com.wixia.rediscache.service;

import com.wixia.rediscache.persistence.CustomerEo;
import com.wixia.rediscache.persistence.CustomerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public List<CustomerEo> findAllDelayable(long delayInMs) {
        return customerRepository.findAllDelayable(delayInMs);
    }

    public CustomerEo findByFirstNameAndLastNameDelayable(String firstName, String lastName, long delayInMs) {
        try {
            return customerRepository.findByFirstNameAndLastNameDelayable(firstName, lastName, delayInMs);
        } catch (RuntimeException ex) {
            // This is a rudimentary failover for cache problems
            logger.warn("Exception when using cacheable call. Trying with a direct call instead.", ex);
            return customerRepository.findByFirstNameAndLastName(firstName, lastName);
        }
    }
}
