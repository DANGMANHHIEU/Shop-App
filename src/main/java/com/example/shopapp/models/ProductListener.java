package com.example.shopapp.models;

import com.example.shopapp.service.product.ProductRedisService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
Install Debezium and configure it to capture changes in the MySQL product table.

Set up a Kafka Connect destination to consume the Debezium change data events.

Implement a Spring Boot application that subscribes to the Kafka Connect destination and updates the Redis cache accordingly.
* */
@AllArgsConstructor
@Service
public class ProductListener {
  @Autowired
    private final ProductRedisService productRedisService;
  //  private static final Logger logger = LoggerFactory.getLogger(ProductListener.class);
    @PrePersist
    public void prePersist(Product product) {
      //  logger.info("prePersist");
    }

    @PostPersist //save = persis
    public void postPersist(Product product) {
        // Update Redis cache
      //  logger.info("postPersist");
        productRedisService.clear();
    }

    @PreUpdate
    public void preUpdate(Product product) {
        //ApplicationEventPublisher.instance().publishEvent(event);
    //    logger.info("preUpdate");
    }

    @PostUpdate
    public void postUpdate(Product product) {
        // Update Redis cache
    //    logger.info("postUpdate");
        productRedisService.clear();
    }

    @PreRemove
    public void preRemove(Product product) {
        //ApplicationEventPublisher.instance().publishEvent(event);
    //    logger.info("preRemove");
    }

    @PostRemove
    public void postRemove(Product product) {
        // Update Redis cache
     //   logger.info("postRemove");
        productRedisService.clear();
    }
}
