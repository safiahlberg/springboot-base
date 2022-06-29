package com.wixia.rediscache.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class CustomerRepositoryImpl implements CustomerRepositorySlow {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Iterable<CustomerEo> findAllDelayable(long delayInMs) {

           delay(delayInMs);

        return entityManager.createQuery("select c from customer c").getResultList();
    }

    @Override
    public List<CustomerEo> findByFirstNameAndLastNameDelayable(String firstName, String lastName, long delayInMs) {
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
