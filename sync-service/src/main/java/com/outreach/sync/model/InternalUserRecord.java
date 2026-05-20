package com.outreach.sync.model;

import java.util.Objects;

public record InternalUserRecord(String id, String firstName, String lastName,
                                 String phoneNumber) implements InternalRecord {

    public static final String TYPE = "user";

    public InternalUserRecord(String id, String firstName, String lastName, String phoneNumber) {
        this.id = Objects.requireNonNull(id, "id");
        this.firstName = Objects.requireNonNull(firstName, "firstName");
        this.lastName = Objects.requireNonNull(lastName, "lastName");
        this.phoneNumber = Objects.requireNonNull(phoneNumber, "phoneNumber");
    }
}
