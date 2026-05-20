package com.outreach.sync.transformer;

import com.outreach.sync.model.InternalRecord;

import java.util.Map;

/**
 * Maps a record from System A's format into the internal model,
 * for one record type.
 */
public interface InboundMapper {

    String getSupportedRecordType();

    InternalRecord map(Map<String, Object> systemAPayload);
}
