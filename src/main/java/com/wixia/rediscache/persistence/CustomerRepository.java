package com.wixia.rediscache.persistence;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends CrudRepository<CustomerEo, Long> {

    List<CustomerEo> findByLastName(String lastName);

    List<CustomerEo> findByFirstName(String firstName);

    CustomerEo findByFirstNameAndLastName(String firstName, String lastName);

    @Query(value = "SELECT c FROM customer c WHERE c.firstName = :searchName or c.lastName = :searchName")
    List<CustomerEo> findByFirstNameOrLastName(@Param("searchName") String searchName);

}
