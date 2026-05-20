package com.outreach.sync.transformer;

import com.outreach.sync.model.InternalRecord;
import com.outreach.sync.model.InternalUserRecord;

import java.util.Map;

public final class UserInboundMapper implements InboundMapper {

    private static final String DEFAULT_COUNTRY_CODE = "+91";

    @Override
    public String getSupportedRecordType() {
        return InternalUserRecord.TYPE;
    }

    @Override
    public InternalRecord map(Map<String, Object> payload) {
        String id = (String) payload.get("user_id");
        String firstName = (String) payload.get("first_name");
        String lastName = (String) payload.get("last_name");
        String phoneNumber = normalizePhone((String) payload.get("phone"));
        return new InternalUserRecord(id, firstName, lastName, phoneNumber);
    }

    private String normalizePhone(String phoneNumber) {
        return DEFAULT_COUNTRY_CODE + phoneNumber;
    }
}
