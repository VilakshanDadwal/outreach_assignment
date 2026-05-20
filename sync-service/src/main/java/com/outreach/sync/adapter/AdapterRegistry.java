package com.outreach.sync.adapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Returns the applicable adapter given the target name.
 */
public final class AdapterRegistry {

    private final Map<String, ExternalSystemAdapter> adaptersByName = new HashMap<>();

    public void register(ExternalSystemAdapter adapter) {
        adaptersByName.put(adapter.getName(), adapter);
    }

    public ExternalSystemAdapter get(String name) {
        return adaptersByName.get(name);
    }
}
