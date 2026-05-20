package com.outreach.sync.mock;

import com.outreach.sync.model.OperationType;
import com.outreach.sync.model.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

/**
 * Mocked system A. It creates the change events and publishes them to the queue
 * so the sync service can consume them. Real System A would do this whenever a
 * record is written to its database.
 */
public class MockedSystemA {

    private static final Logger log = LoggerFactory.getLogger(MockedSystemA.class);

    private final InMemoryQueue queue;

    public MockedSystemA(InMemoryQueue queue) {
        this.queue = queue;
    }

    public void publishCreate(String recordType, String recordId, Map<String, Object> payload) {
        publish(recordType, recordId, OperationType.CREATE, Source.USER, payload);
    }

    public void publishUpdate(String recordType, String recordId, Map<String, Object> payload) {
        publish(recordType, recordId, OperationType.UPDATE, Source.USER, payload);
    }

    public void publishDelete(String recordType, String recordId) {
        publish(recordType, recordId, OperationType.DELETE, Source.USER, Map.of());
    }

    private void publish(String recordType,
                        String recordId,
                        OperationType operation,
                        Source source,
                        Map<String, Object> payload) {
        ChangeEvent event = new ChangeEvent(
                UUID.randomUUID().toString(),
                recordType,
                recordId,
                operation,
                source,
                payload);
        log.info("Emitting change event recordType={} recordId={} operation={} source={} eventId={}",
                event.recordType(), event.recordId(), event.operation(), event.source(), event.eventId());
        queue.publish(event);
    }
}
