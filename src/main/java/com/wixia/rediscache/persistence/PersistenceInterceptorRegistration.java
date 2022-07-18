package com.wixia.rediscache.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PersistenceInterceptorRegistration implements HibernatePropertiesCustomizer {

    @Autowired
    private PersistenceInterceptor persistenceInterceptor;

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put("hibernate.session_factory.interceptor", persistenceInterceptor);
    }
}
