package com.outreach.sync.transformer;

import java.util.HashMap;
import java.util.Map;

/**
 * Resolves the right mapper based on the event's recordType.
 * Adding a new record type means registering a new mapper here.
 */
public final class InboundMapperRegistry {

    private final Map<String, InboundMapper> mappersByRecordType = new HashMap<>();

    public void register(InboundMapper mapper) {
        mappersByRecordType.put(mapper.getSupportedRecordType(), mapper);
    }

    public InboundMapper get(String recordType) {
        return mappersByRecordType.get(recordType);
    }
}
