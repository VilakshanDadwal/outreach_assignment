package com.outreach.sync.mock;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

/**
 * In-memory stand-in for the message queue (e.g., Kafka topic) that sits between
 * System A and the sync service.
 */
public class InMemoryQueue {

    private final Queue<ChangeEvent> queue = new LinkedList<>();

    public void publish(ChangeEvent event) {
        queue.add(event);
    }

    public Optional<ChangeEvent> poll() {
        return Optional.ofNullable(queue.poll());
    }
}
