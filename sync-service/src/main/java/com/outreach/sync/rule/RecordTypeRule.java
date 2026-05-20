package com.outreach.sync.rule;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.outreach.sync.mock.ChangeEvent;

import java.util.List;

/**
 * Routes by record type: applies when the event's recordType matches.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class RecordTypeRule implements Rule {

    private final String recordType;
    private final List<String> targets;

    @JsonCreator
    public RecordTypeRule(@JsonProperty("recordType") String recordType,
                          @JsonProperty("targets") List<String> targets) {
        this.recordType = recordType;
        this.targets = List.copyOf(targets);
    }

    @Override
    public boolean isApplicableTo(ChangeEvent event) {
        return recordType.equals(event.recordType());
    }

    @Override
    public List<String> targets() {
        return targets;
    }
}
