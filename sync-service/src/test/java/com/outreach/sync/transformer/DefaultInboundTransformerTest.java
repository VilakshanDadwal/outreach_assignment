package com.outreach.sync.transformer;

import com.outreach.sync.mock.ChangeEvent;
import com.outreach.sync.model.InternalRecord;
import com.outreach.sync.model.InternalUserRecord;
import com.outreach.sync.model.OperationType;
import com.outreach.sync.model.Source;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultInboundTransformerTest {

    @Mock private InboundMapperRegistry mappers;
    @Mock private InboundMapper mapper;

    private DefaultInboundTransformer transformer;

    private final InternalUserRecord record =
            new InternalUserRecord("U123", "Rahul", "Sharma", "+919876543210");

    @BeforeEach
    void setUp() {
        transformer = new DefaultInboundTransformer(mappers);
    }

    private static ChangeEvent event(OperationType operationType) {
        return new ChangeEvent(UUID.randomUUID().toString(), "user", "U123",
                operationType, Source.USER, Map.of("user_id", "U123"));
    }

    @Test
    void shouldTransformUsingMapperForRecordType() {
        ChangeEvent changeEvent = event(OperationType.CREATE);
        when(mappers.get("user")).thenReturn(mapper);
        when(mapper.map(changeEvent.payload())).thenReturn(record);

        Optional<InternalRecord> result = transformer.transform(changeEvent);

        assertTrue(result.isPresent());
        assertEquals(record, result.get());
    }

    @Test
    void shouldReturnEmptyForDeleteEvent() {
        Optional<InternalRecord> result = transformer.transform(event(OperationType.DELETE));

        assertTrue(result.isEmpty());
        verifyNoInteractions(mappers);
    }
}
