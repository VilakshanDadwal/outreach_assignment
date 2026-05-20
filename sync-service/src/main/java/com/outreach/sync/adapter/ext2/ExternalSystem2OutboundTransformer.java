package com.outreach.sync.adapter.ext2;

import com.outreach.sync.model.InternalUserRecord;

import java.util.HashMap;
import java.util.Map;

public final class ExternalSystem2OutboundTransformer {

    public Map<String, Object> toPayload(InternalUserRecord record) {
        Map<String, Object> body = new HashMap<>();
        body.put("fullName", record.firstName() + " " + record.lastName());
        body.put("phone", record.phoneNumber());
        return body;
    }
}
