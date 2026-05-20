package com.outreach.sync.rule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outreach.sync.mock.ChangeEvent;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Decides which external systems a change event should be routed to.
 */
public final class RuleEngine {

    private final List<Rule> rules;

    public RuleEngine(List<Rule> rules) {
        this.rules = List.copyOf(rules);
    }

    /**
     * Loads the rules from a config file.
     */
    public static RuleEngine loadFromClasspath(String resourceName) {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream in = RuleEngine.class.getClassLoader().getResourceAsStream(resourceName)) {
            RuleConfig config = mapper.readValue(in, RuleConfig.class);
            return new RuleEngine(config.rules());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load rules from " + resourceName, e);
        }
    }

    public List<String> targetsFor(ChangeEvent event) {
        List<String> targets = new ArrayList<>();
        for (Rule rule : rules) {
            if (rule.isApplicableTo(event)) {
                targets.addAll(rule.targets());
            }
        }
        return targets;
    }

    private record RuleConfig(List<Rule> rules) {}
}
