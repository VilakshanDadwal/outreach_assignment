package com.outreach.sync.adapter.ext1;

import com.outreach.sync.adapter.ExternalSystemAdapter;
import com.outreach.sync.mock.MockedExternalSystem;
import com.outreach.sync.model.InternalRecord;
import com.outreach.sync.model.InternalUserRecord;

import java.util.Map;

public final class ExternalSystem1Adapter extends ExternalSystemAdapter {

    public static final String NAME = "ext-1";

    private final ExternalSystem1OutboundTransformer outboundTransformer;

    public ExternalSystem1Adapter(MockedExternalSystem api,
                                  ExternalSystem1OutboundTransformer outboundTransformer) {
        super(NAME, api);
        this.outboundTransformer = outboundTransformer;
    }

    @Override
    protected Map<String, Object> toPayload(InternalRecord record) {
        return outboundTransformer.toPayload((InternalUserRecord) record);
    }
}
