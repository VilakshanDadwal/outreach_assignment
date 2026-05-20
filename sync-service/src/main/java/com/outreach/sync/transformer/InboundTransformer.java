package com.outreach.sync.transformer;

import com.outreach.sync.mock.ChangeEvent;
import com.outreach.sync.model.InternalRecord;

import java.util.Optional;

/**
 * Given a change event, transformer produces the internal record.
 */
public interface InboundTransformer {

    Optional<InternalRecord> transform(ChangeEvent event);
}
