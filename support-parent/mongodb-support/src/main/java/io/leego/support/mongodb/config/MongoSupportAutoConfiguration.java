package io.leego.support.mongodb.config;

import io.leego.support.mongodb.entity.Garbage;
import io.leego.support.mongodb.listener.MongoGarbageCollectionEventListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.IndexResolver;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;

/**
 * @author Leego Yih
 */
@Configuration
public class MongoSupportAutoConfiguration {

    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        // Remove default type mapper
        MappingMongoConverter mappingMongoConverter = applicationContext.getBean(MappingMongoConverter.class);
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        // Ensure indexes
        MongoTemplate mongoTemplate = applicationContext.getBean(MongoTemplate.class);
        IndexResolver indexResolver = new MongoPersistentEntityIndexResolver(mongoTemplate.getConverter().getMappingContext());
        IndexOperations indexOps = mongoTemplate.indexOps(Garbage.class);
        indexResolver.resolveIndexFor(Garbage.class).forEach(indexOps::ensureIndex);
    }

    @Bean
    public MongoGarbageCollectionEventListener mongoGarbageCollectionEventListener(MongoTemplate mongoTemplate) {
        return new MongoGarbageCollectionEventListener(mongoTemplate);
    }

}
