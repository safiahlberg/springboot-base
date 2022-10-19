# Cache poc
Experimenting with Spring Boot caching and Redis as Cache DB.

## Spring cache annotations
Using Spring Cache annotations, cache in Redis is accessed and used to store values for both single objects and collections. The configuration to get Spring Boot to use Redis as cache storage is in itself minimal, but the connection factory is overriden in order to implement a more relaxed dependency on Redis. This means that the original service method is used as fallback not only if a value is missing in Redis, but also if Redis is unresponsive (timeouts can be set to configure how long we want to wait for Redis). Lettuce is governing this and also reconnect logic.

### Structure
The [Customer](src/main/java/com/wixia/rediscache/persistence/CustomerEo.java)
and [Item](src/main/java/com/wixia/rediscache/persistence/ItemEo.java)
are domain objects, in this case represented by JPA-data.
A customer has a relation to a list of items. A H2 instance is set up and
populated with a few example objects. The Customer has a overridden
repository, which can simulate delays dynamically.

## Reactive and caching
The result from a reactive call (Flux or Mono) is not something that is easily cacheable. A version of caching is implemented in parallell with own classes for Controller, Service and Repository, and here the caching is done by hand, with direct communication with Redis.

### Access reactive service
```bash
curl localhost:8080/reactive/customers
```

### Get data in Redis
```shell
for key in $(redis-cli keys '*'); do redis-cli GET $key; done
```

### Delete data in redis
```bash
 for key in $(redis-cli keys '*'); do redis-cli DEL $key; done
 ```

# Copied code from:

* https://docs.liquibase.com/tools-integrations/springboot/springboot.html
* https://spring.io/guides/gs/accessing-data-jpa/
* https://github.com/spring-projects/spring-hateoas-examples/tree/main/simplified
* https://docs.spring.io/spring-hateoas/docs/current/reference/html/
* https://stackoverflow.com/questions/11880924/how-to-add-custom-method-to-spring-data-jpa
* https://www.baeldung.com/spring-boot-redis-cache
* https://www.baeldung.com/jpa-unique-constraints
