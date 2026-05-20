package com.outreach.sync.processor;

import com.outreach.sync.adapter.ExternalSystemClient;
import com.outreach.sync.mock.MockedMappingStore;
import com.outreach.sync.mock.ChangeEvent;
import com.outreach.sync.model.Source;
import com.outreach.sync.model.InternalRecord;
import com.outreach.sync.rule.RuleEngine;
import com.outreach.sync.transformer.InboundTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Processes one change event:
 * validates, transforms, applies rules, and delivers the change to external system
 */
public final class SyncProcessor {

    private static final Logger log = LoggerFactory.getLogger(SyncProcessor.class);

    private final InboundTransformer transformer;
    private final RuleEngine ruleEngine;
    private final ExternalSystemClient externalSystemClient;
    private final MockedMappingStore mappingStore;

    public SyncProcessor(InboundTransformer transformer,
                         RuleEngine ruleEngine,
                         ExternalSystemClient externalSystemClient,
                         MockedMappingStore mappingStore) {
        this.transformer = transformer;
        this.ruleEngine = ruleEngine;
        this.externalSystemClient = externalSystemClient;
        this.mappingStore = mappingStore;
    }

    public void process(ChangeEvent event) {
        log.info("Received change event recordType={} recordId={} operation={} eventId={}",
                event.recordType(), event.recordId(), event.operation(), event.eventId());

        if (event.source() == Source.SYNC_SERVICE) {
            log.info("Discarding echo event eventId={} originated from sync service itself",
                    event.eventId());
            return;
        }

        var record = transformer.transform(event);
        var targets = ruleEngine.targetsFor(event);

        for (String target : targets) {
            dispatch(event, record, target);
        }
    }

    private void dispatch(ChangeEvent event, Optional<InternalRecord> record, String target) {
        var recordId = event.recordId();
        Optional<String> existingExternalId = mappingStore.getExternalId(target, recordId);

        switch (event.operation()) {
            case CREATE -> handleCreate(target, record.orElseThrow(), recordId, existingExternalId);
            case UPDATE -> handleUpdate(target, record.orElseThrow(), existingExternalId);
            case DELETE -> handleDelete(target, recordId, existingExternalId);
        }
    }

    private void handleCreate(String target,
                              InternalRecord record,
                              String recordId,
                              Optional<String> existingExternalId) {
        if (existingExternalId.isPresent()) {
            log.info("Mapping already exists for recordId={} in {} - applying as update for idempotency",
                    recordId, target);
            externalSystemClient.update(target, existingExternalId.get(), record);
            return;
        }
        var externalId = externalSystemClient.create(target, record);
        mappingStore.put(target, recordId, externalId);
        log.info("Stored mapping in {} for recordId={} -> externalId={}",
                target, recordId, externalId);
    }

    private void handleUpdate(String target,
                              InternalRecord record,
                              Optional<String> existingExternalId) {
        externalSystemClient.update(target, existingExternalId.orElseThrow(), record);
    }

    private void handleDelete(String target,
                              String recordId,
                              Optional<String> existingExternalId) {
        externalSystemClient.delete(target, existingExternalId.orElseThrow());
        mappingStore.remove(target, recordId);
        log.info("Removed mapping in {} for recordId={}", target, recordId);
    }
}
