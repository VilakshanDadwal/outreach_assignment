package com.outreach.sync.model;

/**
 * Marker for the sync service's own data model. Each record type (user, order)
 * has its own implementation.
 */
public interface InternalRecord {
    String id();
}
