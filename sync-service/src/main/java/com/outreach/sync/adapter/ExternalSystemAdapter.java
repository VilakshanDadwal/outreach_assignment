package com.outreach.sync.adapter;

import com.outreach.sync.mock.MockedExternalSystem;
import com.outreach.sync.model.InternalRecord;

import java.util.Map;

/**
 * An adapter talks to one external system. The create/update/delete flow is the
 * same for every system — transform the internal record into that system's
 * payload, then call its API.
 */
public abstract class ExternalSystemAdapter {

    private final String name;
    private final MockedExternalSystem externalSystemApi;

    protected ExternalSystemAdapter(String name, MockedExternalSystem externalSystemApi) {
        this.name = name;
        this.externalSystemApi = externalSystemApi;
    }

    public final String getName() {
        return name;
    }

    /** Creates the record in the external system and returns the new external id. */
    public final String create(InternalRecord record) {
        return externalSystemApi.createRecord(toPayload(record));
    }

    public final void update(String externalId, InternalRecord record) {
        externalSystemApi.updateRecord(externalId, toPayload(record));
    }

    public final void delete(String externalId) {
        externalSystemApi.deleteRecord(externalId);
    }

    protected abstract Map<String, Object> toPayload(InternalRecord record);
}
