package com.outreach.sync.transformer;

import com.outreach.sync.model.InternalUserRecord;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserInboundMapperTest {

    private final UserInboundMapper mapper = new UserInboundMapper();

    @Test
    void shouldMapSystemAPayloadToInternalRecord() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("user_id", "U123");
        payload.put("first_name", "Rahul");
        payload.put("last_name", "Sharma");
        payload.put("phone", "9876543210");

        InternalUserRecord record = (InternalUserRecord) mapper.map(payload);

        assertEquals("U123", record.id());
        assertEquals("Rahul", record.firstName());
        assertEquals("Sharma", record.lastName());
        assertEquals("+919876543210", record.phoneNumber(), "phone should get country code");
    }

    @Test
    void shouldSupportUserRecordType() {
        assertEquals("user", mapper.getSupportedRecordType());
    }
}
