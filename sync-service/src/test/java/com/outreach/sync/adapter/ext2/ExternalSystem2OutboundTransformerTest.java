package com.outreach.sync.adapter.ext2;

import com.outreach.sync.model.InternalUserRecord;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExternalSystem2OutboundTransformerTest {

    private final ExternalSystem2OutboundTransformer transformer = new ExternalSystem2OutboundTransformer();

    @Test
    void shouldMapToExternalSystemFormat() {
        Map<String, Object> payload = transformer.toPayload(
                new InternalUserRecord("U123", "Rahul", "Sharma", "+919876543210"));

        assertEquals("Rahul Sharma", payload.get("fullName"));
        assertEquals("+919876543210", payload.get("phone"));
    }
}
