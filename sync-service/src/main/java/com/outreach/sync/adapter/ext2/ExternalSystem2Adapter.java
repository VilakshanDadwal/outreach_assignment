package com.outreach.sync.adapter.ext2;

import com.outreach.sync.adapter.ExternalSystemAdapter;
import com.outreach.sync.mock.MockedExternalSystem;
import com.outreach.sync.model.InternalRecord;
import com.outreach.sync.model.InternalUserRecord;

import java.util.Map;

public final class ExternalSystem2Adapter extends ExternalSystemAdapter {

    public static final String NAME = "ext-2";

    private final ExternalSystem2OutboundTransformer outboundTransformer;

    public ExternalSystem2Adapter(MockedExternalSystem api,
                                  ExternalSystem2OutboundTransformer outboundTransformer) {
        super(NAME, api);
        this.outboundTransformer = outboundTransformer;
    }

    @Override
    protected Map<String, Object> toPayload(InternalRecord record) {
        return outboundTransformer.toPayload((InternalUserRecord) record);
    }
}
