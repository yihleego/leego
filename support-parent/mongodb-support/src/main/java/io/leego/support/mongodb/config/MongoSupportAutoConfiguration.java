package io.leego.support.mongodb.config;

import io.leego.support.mongodb.listener.MongoGarbageCollectionEventListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

/**
 * @author Leego Yih
 */
@Configuration
public class MongoSupportAutoConfiguration {

    @EventListener(ContextRefreshedEvent.class)
    public void initMongoAfterStartup(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        MappingMongoConverter mappingMongoConverter = applicationContext.getBean(MappingMongoConverter.class);
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
    }

    @Bean
    public MongoGarbageCollectionEventListener mongoGarbageCollectionEventListener(MongoTemplate mongoTemplate) {
        return new MongoGarbageCollectionEventListener(mongoTemplate);
    }

}
