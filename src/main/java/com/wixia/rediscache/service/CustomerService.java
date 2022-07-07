package com.wixia.rediscache.service;

import com.wixia.rediscache.persistence.CustomerEo;
import com.wixia.rediscache.persistence.CustomerRepository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Optional<CustomerEo> findById(long id) {
        return customerRepository.findById(id);
    }

        public Iterable<CustomerEo> findAllDelayable(long delayInMs) {
        try {
            return customerRepository.findAllDelayable(delayInMs);
        } catch (RedisConnectionFailureException ex) {
            return customerRepository.findAll();
        }
    }
}
