package com.outreach.sync.adapter.ext1;

import com.outreach.sync.model.InternalUserRecord;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExternalSystem1OutboundTransformerTest {

    private final ExternalSystem1OutboundTransformer transformer = new ExternalSystem1OutboundTransformer();

    @Test
    void shouldMapToExternalSystemWithIndianCountryCodeRemoved() {
        Map<String, Object> payload = transformer.toPayload(
                new InternalUserRecord("U123", "Rahul", "Sharma", "+919876543210"));

        assertEquals("Rahul", payload.get("first"));
        assertEquals("Sharma", payload.get("last"));
        assertEquals("9876543210", payload.get("contact_phone"));
    }

    @Test
    void shouldKeepPhoneUnchangedWhenNotIndianNumber() {
        Map<String, Object> payload = transformer.toPayload(
                new InternalUserRecord("U1", "John", "Doe", "+14155550100"));

        assertEquals("+14155550100", payload.get("contact_phone"));
    }
}
