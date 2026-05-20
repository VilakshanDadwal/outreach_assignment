package com.outreach.sync.mock;

import com.outreach.sync.model.OperationType;
import com.outreach.sync.model.Source;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * A change event emitted by System A. The payload is the raw record in System A's
 * format for CREATE/UPDATE; for DELETE only recordId is meaningful and payload is empty.
 */
public record ChangeEvent(String eventId, String recordType, String recordId, OperationType operation, Source source,
                          Map<String, Object> payload) {

    public ChangeEvent(String eventId,
                       String recordType,
                       String recordId,
                       OperationType operation,
                       Source source,
                       Map<String, Object> payload) {
        this.eventId = Objects.requireNonNull(eventId, "eventId");
        this.recordType = Objects.requireNonNull(recordType, "recordType");
        this.recordId = Objects.requireNonNull(recordId, "recordId");
        this.operation = Objects.requireNonNull(operation, "operation");
        this.source = Objects.requireNonNull(source, "source");
        this.payload = payload == null ? Map.of() : Collections.unmodifiableMap(payload);
    }
}
