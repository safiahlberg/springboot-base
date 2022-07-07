package com.wixia.rediscache.persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends CrudRepository<CustomerEo, Long>, CustomerRepositoryCacheable {

    List<CustomerEo> findByLastName(String lastName);

    List<CustomerEo> findByFirstName(String firstName);

    List<CustomerEo> findByFirstNameAndLastName(String firstName, String lastName);

    @Query("select c from customer c where c.firstName = :searchName or c.lastName = :searchName")
    List<CustomerEo> findByFirstNameOrLastName(@Param("searchName") String searchName);
}
