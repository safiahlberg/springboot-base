package com.wixia.rediscache.persistence;

import org.hibernate.EmptyInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PersistenceInterceptor extends EmptyInterceptor {

    private final Logger logger = LoggerFactory.getLogger(PersistenceInterceptor.class);

    @Override
    public String onPrepareStatement(String sql) {

        if (sql.contains("select customer")) {
            logger.info("Simulating delay on, onPrepareStatement: {}", sql);
            delay(7000);
        }

        return super.onPrepareStatement(sql);
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
