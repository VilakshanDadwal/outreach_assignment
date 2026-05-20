package com.outreach.sync.rule;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.outreach.sync.mock.ChangeEvent;

import java.util.List;

/**
 * Routes by a "region" field in the event payload, regardless of record type.
 */

public final class RegionRule implements Rule {

    private final String region;
    private final List<String> targets;

    @JsonCreator
    public RegionRule(@JsonProperty("region") String region,
                      @JsonProperty("targets") List<String> targets) {
        this.region = region;
        this.targets = List.copyOf(targets);
    }

    @Override
    public boolean isApplicableTo(ChangeEvent event) {
        return region.equals(event.payload().get("region"));
    }

    @Override
    public List<String> targets() {
        return targets;
    }
}
