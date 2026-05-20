package com.outreach.sync.rule;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.outreach.sync.mock.ChangeEvent;

import java.util.List;

/**
 * Rule decides whether it applies to a given change
 * event and which external systems it routes to.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RecordTypeRule.class, name = "byRecordType"),
        @JsonSubTypes.Type(value = RegionRule.class, name = "byRegion")
})
public interface Rule {

    boolean isApplicableTo(ChangeEvent event);

    List<String> targets();
}
