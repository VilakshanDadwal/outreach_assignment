package com.outreach.sync.adapter.ext1;

import com.outreach.sync.mock.MockedExternalSystem;
import com.outreach.sync.model.InternalUserRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalSystem1AdapterTest {

    @Mock private MockedExternalSystem api;
    @Mock private ExternalSystem1OutboundTransformer outboundTransformer;

    private ExternalSystem1Adapter adapter;

    private final InternalUserRecord record =
            new InternalUserRecord("U123", "Rahul", "Sharma", "+919876543210");
    private final Map<String, Object> transformedPayload = Map.of("first", "Rahul");

    @BeforeEach
    void setUp() {
        adapter = new ExternalSystem1Adapter(api, outboundTransformer);
    }

    @Test
    void shouldReturnExternalIdOnCreate() {
        when(outboundTransformer.toPayload(record)).thenReturn(transformedPayload);
        when(api.createRecord(transformedPayload)).thenReturn("ext-1-aaa");

        String externalId = adapter.create(record);

        assertEquals("ext-1-aaa", externalId);
        verify(api).createRecord(transformedPayload);
    }

    @Test
    void shouldUpdateExistingRecordOnUpdate() {
        when(outboundTransformer.toPayload(record)).thenReturn(transformedPayload);

        adapter.update("ext-1-aaa", record);

        verify(api).updateRecord("ext-1-aaa", transformedPayload);
    }

    @Test
    void shouldDeleteExternalRecord() {
        adapter.delete("ext-1-aaa");

        verify(api).deleteRecord("ext-1-aaa");
    }
}
