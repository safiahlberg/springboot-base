package com.wixia.rediscache.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CustomerRepositoryImpl implements CustomerRepositorySlow {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Iterable<CustomerEo> findAllSlow() {
        delay();
        return entityManager.createQuery("select c from customer c").getResultList();
    }

    private void delay() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
