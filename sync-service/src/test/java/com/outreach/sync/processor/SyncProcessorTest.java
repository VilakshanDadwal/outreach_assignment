package com.outreach.sync.processor;

import com.outreach.sync.adapter.ExternalSystemClient;
import com.outreach.sync.mock.ChangeEvent;
import com.outreach.sync.mock.MockedMappingStore;
import com.outreach.sync.model.InternalRecord;
import com.outreach.sync.model.InternalUserRecord;
import com.outreach.sync.model.OperationType;
import com.outreach.sync.model.Source;
import com.outreach.sync.rule.RuleEngine;
import com.outreach.sync.transformer.InboundTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SyncProcessorTest {

    @Mock private InboundTransformer transformer;
    @Mock private RuleEngine ruleEngine;
    @Mock private ExternalSystemClient externalSystemClient;
    @Mock private MockedMappingStore mappingStore;

    private SyncProcessor processor;

    private final InternalUserRecord record =
            new InternalUserRecord("U123", "Rahul", "Sharma", "+919876543210");

    @BeforeEach
    void setUp() {
        processor = new SyncProcessor(transformer, ruleEngine, externalSystemClient, mappingStore);
    }

    private static ChangeEvent event(OperationType operationType, Source source) {
        return new ChangeEvent(UUID.randomUUID().toString(), "user", "U123", operationType, source, Map.of());
    }

    @Test
    void shouldCreateAndStoreMappingForEachTarget() {
        ChangeEvent changeEvent = event(OperationType.CREATE, Source.USER);
        when(transformer.transform(changeEvent)).thenReturn(Optional.of(record));
        when(ruleEngine.targetsFor(changeEvent)).thenReturn(List.of("ext-1", "ext-2"));
        when(mappingStore.getExternalId("ext-1", "U123")).thenReturn(Optional.empty());
        when(mappingStore.getExternalId("ext-2", "U123")).thenReturn(Optional.empty());
        when(externalSystemClient.create("ext-1", record)).thenReturn("ext-1-aaa");
        when(externalSystemClient.create("ext-2", record)).thenReturn("ext-2-bbb");

        processor.process(changeEvent);

        verify(externalSystemClient).create("ext-1", record);
        verify(externalSystemClient).create("ext-2", record);
        verify(mappingStore).put("ext-1", "U123", "ext-1-aaa");
        verify(mappingStore).put("ext-2", "U123", "ext-2-bbb");
    }

    @Test
    void shouldDiscardEchoEvent() {
        processor.process(event(OperationType.CREATE, Source.SYNC_SERVICE));

        verifyNoInteractions(transformer, ruleEngine, externalSystemClient, mappingStore);
    }

    @Test
    void shouldUpdateUsingStoredExternalId() {
        ChangeEvent changeEvent = event(OperationType.UPDATE, Source.USER);
        when(transformer.transform(changeEvent)).thenReturn(Optional.of(record));
        when(ruleEngine.targetsFor(changeEvent)).thenReturn(List.of("ext-1"));
        when(mappingStore.getExternalId("ext-1", "U123")).thenReturn(Optional.of("ext-1-aaa"));

        processor.process(changeEvent);

        verify(externalSystemClient).update("ext-1", "ext-1-aaa", record);
    }

    @Test
    void shouldDeleteRecordAndRemoveMapping() {
        ChangeEvent changeEvent = event(OperationType.DELETE, Source.USER);
        when(transformer.transform(changeEvent)).thenReturn(Optional.empty());
        when(ruleEngine.targetsFor(changeEvent)).thenReturn(List.of("ext-1"));
        when(mappingStore.getExternalId("ext-1", "U123")).thenReturn(Optional.of("ext-1-aaa"));

        processor.process(changeEvent);

        verify(externalSystemClient).delete("ext-1", "ext-1-aaa");
        verify(mappingStore).remove("ext-1", "U123");
    }

    @Test
    void shouldTreatDuplicateCreateAsUpdate() {
        ChangeEvent ev = event(OperationType.CREATE, Source.USER);
        when(transformer.transform(ev)).thenReturn(Optional.of(record));
        when(ruleEngine.targetsFor(ev)).thenReturn(List.of("ext-1"));
        when(mappingStore.getExternalId("ext-1", "U123")).thenReturn(Optional.of("ext-1-aaa"));

        processor.process(ev);

        verify(externalSystemClient).update("ext-1", "ext-1-aaa", record);
        verify(externalSystemClient, never()).create(any(), any());
        verify(mappingStore, never()).put(any(), any(), any());
    }
}
