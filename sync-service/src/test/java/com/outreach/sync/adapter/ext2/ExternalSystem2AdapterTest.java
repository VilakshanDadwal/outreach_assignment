package com.outreach.sync.adapter.ext2;

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
class ExternalSystem2AdapterTest {

    @Mock private MockedExternalSystem api;
    @Mock private ExternalSystem2OutboundTransformer outboundTransformer;

    private ExternalSystem2Adapter adapter;

    private final InternalUserRecord record =
            new InternalUserRecord("U123", "Rahul", "Sharma", "+919876543210");
    private final Map<String, Object> transformedPayload = Map.of("fullName", "Rahul Sharma");

    @BeforeEach
    void setUp() {
        adapter = new ExternalSystem2Adapter(api, outboundTransformer);
    }

    @Test
    void shouldReturnExternalIdOnCreate() {
        when(outboundTransformer.toPayload(record)).thenReturn(transformedPayload);
        when(api.createRecord(transformedPayload)).thenReturn("ext-2-bbb");

        String externalId = adapter.create(record);

        assertEquals("ext-2-bbb", externalId);
        verify(api).createRecord(transformedPayload);
    }

    @Test
    void shouldUpdateExistingRecordOnUpdate() {
        when(outboundTransformer.toPayload(record)).thenReturn(transformedPayload);

        adapter.update("ext-2-bbb", record);

        verify(api).updateRecord("ext-2-bbb", transformedPayload);
    }

    @Test
    void shouldDeleteExternalRecord() {
        adapter.delete("ext-2-bbb");

        verify(api).deleteRecord("ext-2-bbb");
    }
}
