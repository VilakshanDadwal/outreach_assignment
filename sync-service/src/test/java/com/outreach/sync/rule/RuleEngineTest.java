package com.outreach.sync.rule;

import com.outreach.sync.mock.ChangeEvent;
import com.outreach.sync.model.OperationType;
import com.outreach.sync.model.Source;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RuleEngineTest {

    private static ChangeEvent event(String recordType, Map<String, Object> payload) {
        return new ChangeEvent(UUID.randomUUID().toString(), recordType, "R1",
                OperationType.CREATE, Source.USER, payload);
    }

    @Test
    void shouldReturnTargetsForMatchingRecordType() {
        RuleEngine ruleEngine = new RuleEngine(List.of(
                new RecordTypeRule("user", List.of("ext-1", "ext-2")),
                new RecordTypeRule("order", List.of("ext-1"))));

        assertEquals(List.of("ext-1", "ext-2"), ruleEngine.targetsFor(event("user", Map.of())));
        assertEquals(List.of("ext-1"), ruleEngine.targetsFor(event("order", Map.of())));
    }

    @Test
    void shouldReturnNoTargetsForUnknownRecordType() {
        RuleEngine ruleEngine = new RuleEngine(List.of(
                new RecordTypeRule("user", List.of("ext-1"))));

        assertTrue(ruleEngine.targetsFor(event("unknown", Map.of())).isEmpty());
    }

    @Test
    void shouldAggregateTargetsFromAllMatchingRules() {
        RuleEngine ruleEngine = new RuleEngine(List.of(
                new RecordTypeRule("user", List.of("ext-1")),
                new RecordTypeRule("user", List.of("ext-2"))));

        assertEquals(List.of("ext-1", "ext-2"), ruleEngine.targetsFor(event("user", Map.of())));
    }
}
