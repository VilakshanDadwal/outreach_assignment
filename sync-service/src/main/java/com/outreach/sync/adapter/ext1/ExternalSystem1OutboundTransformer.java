package com.outreach.sync.adapter.ext1;

import com.outreach.sync.model.InternalUserRecord;

import java.util.HashMap;
import java.util.Map;

public final class ExternalSystem1OutboundTransformer {

    public Map<String, Object> toPayload(InternalUserRecord record) {
        Map<String, Object> body = new HashMap<>();
        body.put("first", record.firstName());
        body.put("last", record.lastName());
        body.put("contact_phone", removeCountryCode(record.phoneNumber()));
        return body;
    }

    private String removeCountryCode(String phone) {
        if (phone.startsWith("+91")) return phone.substring(3);
        return phone;
    }
}
