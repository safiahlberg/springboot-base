package com.wixia.rediscache.persistence;

public interface CustomerRepositorySlow {

    Iterable<CustomerEo> findAllSlow();

}
