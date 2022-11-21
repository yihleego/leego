package io.leego.support.mongodb.listener;

import io.leego.support.mongodb.entity.Garbage;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Instant;
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
        if (event.getType() == null || event.getDocument() == null) {
            return;
        }
        String collectionName = StringUtils.hasText(event.getCollectionName())
                ? event.getCollectionName()
                : mongoTemplate.getCollectionName(event.getType());
        // Query the objects to be deleted
        List<Object> objects = mongoTemplate.find(new BasicQuery(event.getDocument()), event.getType(), collectionName);
        if (!CollectionUtils.isEmpty(objects)) {
            // Collect the garbage
            Instant now = Instant.now();
            mongoTemplate.insert(objects.stream()
                    .map(o -> new Garbage(collectionName, o, now))
                    .collect(Collectors.toList()), Garbage.class);
        }
    }
}
