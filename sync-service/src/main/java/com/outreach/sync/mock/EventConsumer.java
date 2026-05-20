package com.outreach.sync.mock;

import com.outreach.sync.processor.SyncProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Consumes events from the in-memory queue and hands them over to the processor.
 */
public final class EventConsumer {

    private static final Logger log = LoggerFactory.getLogger(EventConsumer.class);

    private final InMemoryQueue queue;
    private final SyncProcessor processor;

    public EventConsumer(InMemoryQueue queue, SyncProcessor processor) {
        this.queue = queue;
        this.processor = processor;
    }

    public void consume() {
        while (true) {
            Optional<ChangeEvent> next = queue.poll();
            if (next.isEmpty()) return;
            try {
                processor.process(next.get());
            } catch (RuntimeException e) {
                log.error("Processor failed processing eventId={}: {}", next.get().eventId(), e.toString());
            }
        }
    }
}
