package com.outreach.sync.transformer;

import com.outreach.sync.mock.ChangeEvent;
import com.outreach.sync.model.OperationType;
import com.outreach.sync.model.InternalRecord;

import java.util.Optional;

/**
 * Finds the transformer to use for the record type, and does the transformation.
 */
public final class DefaultInboundTransformer implements InboundTransformer {

    private final InboundMapperRegistry mappers;

    public DefaultInboundTransformer(InboundMapperRegistry mappers) {
        this.mappers = mappers;
    }

    @Override
    public Optional<InternalRecord> transform(ChangeEvent event) {
        if (event.operation() == OperationType.DELETE) {
            return Optional.empty();
        }
        return Optional.of(mappers.get(event.recordType()).map(event.payload()));
    }
}
