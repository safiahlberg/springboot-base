package com.wixia.rediscache.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * To simulate delay, in order to test cache effects.
 */
public class CustomerRepositoryImpl implements CustomerRepositoryCacheable {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CustomerEo> findAllCacheable(long delayInMs) {

           delay(delayInMs);

        return entityManager.createQuery("select c from customer c").getResultList();
    }

    @Override
    public List<CustomerEo> findByFirstNameAndLastNameCacheable(String firstName, String lastName, long delayInMs) {
        delay(delayInMs);

        return entityManager.createQuery(
                "select c from customer c where c.firstName = :searchName or c.lastName = :searchName")
                .getResultList();
    }

    private void delay(long delayInMs) {
        if (delayInMs > 0) {
            try {
                Thread.sleep(delayInMs);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
