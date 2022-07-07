package com.wixia.rediscache.persistence;

import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface CustomerRepositorySlow {

    @Cacheable(value = "customerCache")
    Iterable<CustomerEo> findAllDelayable(long delayInMs);

    @Cacheable(value = "customerCache")
    List<CustomerEo> findByFirstNameAndLastNameDelayable(String firstName, String lastName, long delayInMs);


}
