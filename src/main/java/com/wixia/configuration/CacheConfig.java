package com.wixia.configuration;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

/**
 * https://stackoverflow.com/questions/26021991/spring-redis-error-handle
 *
 * Cache properties
 * https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html#appendix.application-properties.cache
 */
@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport implements CachingConfigurer {

    @Value("${redis.hostname:localhost}")
    private String redisHost;

    @Value("${redis.port:6379}")
    private int redisPort;

    @Value("${redis.timeout.secs:1}")
    private int redisTimeoutInSecs;

    @Value("${redis.socket.timeout.secs:1}")
    private int redisSocketTimeoutInSecs;

    @Value("${redis.ttl.hours:1}")
    private int redisDataTTL;

/*    @Bean
    public RedisCacheManager redisCacheManager(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder
            .fromConnectionFactory(lettuceConnectionFactory)
            .cacheDefaults(cacheConfiguration());

        return builder.build();
    }*/

    @Bean
    public RedisCacheManager redisCacheManager(LettuceConnectionFactory lettuceConnectionFactory) {

        /**
         * If we want to use JSON Serialized with own object mapper then use the below config snippet
         */
        // RedisCacheConfiguration redisCacheConfiguration =
        // RedisCacheConfiguration.defaultCacheConfig().disableCachingNullValues()
        // .entryTtl(Duration.ofHours(redisDataTTL)).serializeValuesWith(RedisSerializationContext.SerializationPair
        // .fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)));

        RedisCacheConfiguration redisCacheConfiguration = cacheConfiguration(); /*RedisCacheConfiguration.defaultCacheConfig().disableCachingNullValues()
            .entryTtl(Duration.ofHours(redisDataTTL))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.java()));*/

        redisCacheConfiguration.usePrefix();

        RedisCacheManager redisCacheManager = RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(lettuceConnectionFactory)
            .cacheDefaults(redisCacheConfiguration).build();

        redisCacheManager.setTransactionAware(true);
        return redisCacheManager;
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        // LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
        // .commandTimeout(Duration.ofSeconds(redisConnectionTimeoutInSecs)).shutdownTimeout(Duration.ZERO).build();
        //
        // return new LettuceConnectionFactory(new RedisStandaloneConfiguration(redisHost, redisPort), clientConfig);

        final SocketOptions socketOptions = SocketOptions.builder()
            .connectTimeout(Duration.ofSeconds(redisSocketTimeoutInSecs)).build();

        final ClientOptions clientOptions = ClientOptions.builder().socketOptions(socketOptions).build();

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
            .commandTimeout(Duration.ofSeconds(redisTimeoutInSecs)).clientOptions(clientOptions).build();
        RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration(redisHost, redisPort);

        final LettuceConnectionFactory lettuceConnectionFactory =
            new LettuceConnectionFactory(serverConfig, clientConfig);
        lettuceConnectionFactory.setValidateConnection(true);
        return lettuceConnectionFactory;

    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<Object, Object>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(60))
            .disableCachingNullValues()
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                new GenericJackson2JsonRedisSerializer()));
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new RedisCacheErrorHandler();
    }

}
