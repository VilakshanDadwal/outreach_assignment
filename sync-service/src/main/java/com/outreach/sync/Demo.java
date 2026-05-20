package com.outreach.sync;

import com.outreach.sync.adapter.AdapterRegistry;
import com.outreach.sync.adapter.ExternalSystemClient;
import com.outreach.sync.adapter.DefaultExternalSystemClient;
import com.outreach.sync.adapter.ext1.ExternalSystem1Adapter;
import com.outreach.sync.adapter.ext1.ExternalSystem1OutboundTransformer;
import com.outreach.sync.adapter.ext2.ExternalSystem2Adapter;
import com.outreach.sync.adapter.ext2.ExternalSystem2OutboundTransformer;
import com.outreach.sync.mock.MockedMappingStore;
import com.outreach.sync.mock.InMemoryQueue;
import com.outreach.sync.mock.MockedExternalSystem;
import com.outreach.sync.mock.MockedSystemA;
import com.outreach.sync.model.InternalUserRecord;
import com.outreach.sync.mock.EventConsumer;
import com.outreach.sync.processor.SyncProcessor;
import com.outreach.sync.rule.RuleEngine;
import com.outreach.sync.transformer.InboundMapperRegistry;
import com.outreach.sync.transformer.InboundTransformer;
import com.outreach.sync.transformer.DefaultInboundTransformer;
import com.outreach.sync.transformer.UserInboundMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * End-to-end demo of the A -> B sync.
 *
 * Wires the components together, then walks through a few scenarios printing
 * what happens at each step. After each scenario the final state of both
 * external systems is printed to confirm that both A and B are in sync.
 */
public final class Demo {

    public static void main(String[] args) {
        InMemoryQueue queue = new InMemoryQueue();
        MockedSystemA systemA = new MockedSystemA(queue);

        InboundMapperRegistry mapperRegistry = new InboundMapperRegistry();
        mapperRegistry.register(new UserInboundMapper());
        InboundTransformer transformer = new DefaultInboundTransformer(mapperRegistry);

        RuleEngine ruleEngine = RuleEngine.loadFromClasspath("sync-rules.json");

        MockedExternalSystem externalSystem1 = new MockedExternalSystem(ExternalSystem1Adapter.NAME);
        MockedExternalSystem externalSystem2 = new MockedExternalSystem(ExternalSystem2Adapter.NAME);

        AdapterRegistry adapterRegistry = new AdapterRegistry();
        adapterRegistry.register(new ExternalSystem1Adapter(externalSystem1, new ExternalSystem1OutboundTransformer()));
        adapterRegistry.register(new ExternalSystem2Adapter(externalSystem2, new ExternalSystem2OutboundTransformer()));

        ExternalSystemClient externalSystemClient = new DefaultExternalSystemClient(adapterRegistry);

        MockedMappingStore mappingStore = new MockedMappingStore();

        SyncProcessor processor = new SyncProcessor(
                transformer, ruleEngine, externalSystemClient, mappingStore);

        EventConsumer consumer = new EventConsumer(queue, processor);

        System.out.println("Scenario 1: CREATE a user in System A");
        systemA.publishCreate(InternalUserRecord.TYPE, "U123", createUserPayload("U123", "Rahul", "Sharma", "9876543210"));
        consumer.consume();
        printExternalState(externalSystem1, externalSystem2);
        System.out.println();

        System.out.println("Scenario 2: UPDATE phone number of the same user (phone change)");
        systemA.publishUpdate(InternalUserRecord.TYPE, "U123", createUserPayload("U123", "Rahul", "Sharma", "9999988888"));
        consumer.consume();
        printExternalState(externalSystem1, externalSystem2);
        System.out.println();

        System.out.println("Scenario 3: DELETE the user");
        systemA.publishDelete(InternalUserRecord.TYPE, "U123");
        consumer.consume();
        printExternalState(externalSystem1, externalSystem2);

        System.out.println();
        System.out.println("Demo complete.");
    }

    private static Map<String, Object> createUserPayload(String userId, String first, String last, String phone) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("user_id", userId);
        payload.put("first_name", first);
        payload.put("last_name", last);
        payload.put("phone", phone);
        return payload;
    }

    private static void printExternalState(MockedExternalSystem externalSystem1, MockedExternalSystem externalSystem2) {
        System.out.println();
        System.out.println("--- State of " + externalSystem1.name() + " ---");
        externalSystem1.getRecords().forEach((id, body) -> System.out.println("  " + id + " => " + body));
        if (externalSystem1.getRecords().isEmpty()) System.out.println("No records found !!!");

        System.out.println("--- State of " + externalSystem2.name() + " ---");
        externalSystem2.getRecords().forEach((id, body) -> System.out.println("  " + id + " => " + body));
        if (externalSystem2.getRecords().isEmpty()) System.out.println("No records found !!!");
    }
}
