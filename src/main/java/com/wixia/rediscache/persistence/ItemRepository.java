package com.wixia.rediscache.persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemRepository extends CrudRepository<ItemEo, Long> {

    List<ItemEo> findByName(String name);

    @Query("select i from item i where i.price >= :lowerBound and i.price <= :upperBound")
    List<ItemEo> findInRange(int lowerBound, int upperBound);
}
