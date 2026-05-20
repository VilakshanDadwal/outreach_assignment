package com.outreach.sync.adapter;

import com.outreach.sync.model.InternalRecord;

/**
 * Interface to send messages to external system.
 */
public interface ExternalSystemClient {

    /** Creates the record in the target system and returns its new external id. */
    String create(String target, InternalRecord record);

    void update(String target, String externalId, InternalRecord record);

    void delete(String target, String externalId);
}
