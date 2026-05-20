package com.outreach.sync.mock;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Tracks the relationship between a System A record ID and the corresponding ID
 * in each external system.
 */
public final class MockedMappingStore {

    private final Map<Key, String> mappings = new HashMap<>();

    public void put(String externalSystem, String internalRecordId, String externalRecordId) {
        mappings.put(new Key(externalSystem, internalRecordId), externalRecordId);
    }

    public Optional<String> getExternalId(String externalSystemName, String internalRecordId) {
        return Optional.ofNullable(mappings.get(new Key(externalSystemName, internalRecordId)));
    }

    public void remove(String externalSystemName, String internalRecordId) {
        mappings.remove(new Key(externalSystemName, internalRecordId));
    }

    private record Key(String externalSystemName, String internalRecordId) {}
}
