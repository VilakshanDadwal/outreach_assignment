package com.outreach.sync.adapter;

import com.outreach.sync.model.InternalRecord;

/**
 * Resolves the target name to an adapter and delegates the call to it.
 */
public final class DefaultExternalSystemClient implements ExternalSystemClient {

    private final AdapterRegistry adapterRegistry;

    public DefaultExternalSystemClient(AdapterRegistry adapterRegistry) {
        this.adapterRegistry = adapterRegistry;
    }

    @Override
    public String create(String target, InternalRecord record) {
        return adapterRegistry.get(target).create(record);
    }

    @Override
    public void update(String target, String externalId, InternalRecord record) {
        adapterRegistry.get(target).update(externalId, record);
    }

    @Override
    public void delete(String target, String externalId) {
        adapterRegistry.get(target).delete(externalId);
    }
}
