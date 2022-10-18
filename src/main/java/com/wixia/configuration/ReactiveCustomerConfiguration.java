package com.wixia.configuration;

import com.wixia.domain.Customer;

import io.lettuce.core.ClientOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.time.Duration;

/**
 * https://spring.io/guides/gs/spring-data-reactive-redis/
 */
@Configuration
public class ReactiveCustomerConfiguration {

    public static final String REACTIVE_CF = "reactive-cf";

    @Value("${redis.timeout.secs:1}")
    private int redisTimeoutInSecs;

    @Value("${redis.ttl.minutes:1}")
    private int redisDataTTL;

    @Resource
    private ClientOptions clientOptions;

    @Resource
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory(
        RedisStandaloneConfiguration redisStandaloneConfiguration) {

        LettuceClientConfiguration configuration = LettuceClientConfiguration.builder()
            .commandTimeout(Duration.ofSeconds(redisTimeoutInSecs))
            .clientOptions(clientOptions).build();

        return new LettuceConnectionFactory(redisStandaloneConfiguration, configuration);
    }

    @Bean
    ReactiveRedisOperations<String, Customer> redisOperations(
        ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<Customer> serializer = new Jackson2JsonRedisSerializer<>(Customer.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, Customer> builder =
            RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, Customer> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}
