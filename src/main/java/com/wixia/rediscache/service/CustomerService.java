package com.wixia.rediscache.service;

import com.wixia.rediscache.persistence.CustomerEo;
import com.wixia.rediscache.persistence.CustomerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

        public Iterable<CustomerEo> findAllDelayable(long delayInMs) {
        try {
            return customerRepository.findAllCacheable(delayInMs);
        } catch (RuntimeException ex) {
            logger.warn("Exception when communicating with Redis. Using direct call instead of cached call.", ex);
            return customerRepository.findAll();
        }
    }
}
