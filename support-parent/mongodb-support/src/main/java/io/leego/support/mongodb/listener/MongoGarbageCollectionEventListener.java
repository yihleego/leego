package io.leego.support.mongodb.listener;

import io.leego.support.mongodb.entity.Garbage;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Leego Yih
 */
public class MongoGarbageCollectionEventListener extends AbstractMongoEventListener<Object> {
    private final MongoTemplate mongoTemplate;

    public MongoGarbageCollectionEventListener(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Object> event) {
        if (event.getType() == null || CollectionUtils.isEmpty(event.getDocument())) {
            return;
        }
        // Query the objects to be deleted
        List<Object> objects = mongoTemplate.find(new BasicQuery(event.getDocument()), event.getType());
        if (!CollectionUtils.isEmpty(objects)) {
            // Collect the garbage
            String type = event.getCollectionName();
            LocalDateTime now = LocalDateTime.now();
            mongoTemplate.insertAll(objects.stream()
                    .map(o -> new Garbage(type, o, now))
                    .collect(Collectors.toList()));
        }
    }
}
