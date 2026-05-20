package com.outreach.sync.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class MockedExternalSystem {

    private static final Logger log = LoggerFactory.getLogger(MockedExternalSystem.class);

    private final String name;
    private final Map<String, Map<String, Object>> store = new HashMap<>();

    public MockedExternalSystem(String name) {
        this.name = name;
    }

    public String createRecord(Map<String, Object> payload) {
        String id = name + "-" + UUID.randomUUID().toString().substring(0, 8);
        store.put(id, new HashMap<>(payload));
        log.info("Created record in {} id={} payload={}", name, id, payload);
        return id;
    }

    public void updateRecord(String id, Map<String, Object> payload) {
        if (!store.containsKey(id)) {
            log.warn("Cannot update record in {} id={}: record not found", name, id);
            throw new RuntimeException("record not found: " + id);
        }
        store.put(id, new HashMap<>(payload));
        log.info("Updated record in {} id={} payload={}", name, id, payload);
    }

    public void deleteRecord(String id) {
        if (store.remove(id) == null) {
            log.info("Delete is invalid for {} id={}: record already absent", name, id);
            return;
        }
        log.info("Deleted record from {} id={}", name, id);
    }

    public Map<String, Map<String, Object>> getRecords() {
        return new HashMap<>(store);
    }

    public String name() { return name; }
}
