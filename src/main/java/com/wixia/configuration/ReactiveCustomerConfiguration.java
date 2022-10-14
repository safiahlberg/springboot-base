package com.wixia.configuration;

import com.wixia.domain.Customer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * https://spring.io/guides/gs/spring-data-reactive-redis/
 */
@Configuration
public class ReactiveCustomerConfiguration {

    @Bean
    ReactiveRedisOperations<String, Customer> redisOperations(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<Customer> serializer = new Jackson2JsonRedisSerializer<>(Customer.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, Customer> builder =
            RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, Customer> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}
