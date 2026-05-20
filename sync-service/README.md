# Sync Service (A → B)

A small, runnable implementation of the **A → B direction** of the
record-to-record synchronization service.

## What it does

When a record changes in **System A** (internal), a change event is published
to a queue. The sync service consumes the event, transforms the data into an
internal model, decides which external system it should go to based on
configurable rules, converts it into each target system's format, and delivers
it via that system's adapter. It keeps a mapping between internal and external
record ids so updates and deletes hit the right external record, and so
redelivered events stay idempotent.

System A, the message queue, the external systems, and
the mapping store are all **mocked in-process** so the whole flow runs locally
with no external dependencies. 

Validation of the incoming and outgoing request has not been included for simplicity.
Its covered in the design.

## Prerequisites

- Java 17+
- Maven 3.5+

## Build & test

```bash
mvn test
```

Runs the tests.

## Run the demo

```bash
mvn compile exec:exec
```

This runs `com.outreach.sync.Demo`, which wires the components together and
walks through create, update and delete scenarios. 
After each scenario it prints the state of both mock external systems 
so you can see System A and the external systems staying
in sync.

## Configuration

Routing rules live in [`src/main/resources/sync-rules.json`](src/main/resources/sync-rules.json).
Each rule maps a condition (e.g. record type) to the external systems that
should receive the change. Editing this file changes routing without code
changes.

## Project layout

```
src/main/java/com/outreach/sync
├── Demo.java         End-to-end runnable demo
├── processor/        SyncProcessor — orchestrates syncing of one event
├── transformer/      Inbound transformation (System A format → internal model)
├── rule/             RuleEngine — decides target external systems
├── adapter/          External system adapters (outbound transform + API call)
├── model/            Internal domain model and event enums
└── mock/             In-memory stand-ins: queue, System A, external systems,
                       mapping store, event consumer
```

## Extending

- **New external system:** add an `ExternalSystemAdapter` subclass and register
  it; add a rule in `sync-rules.json`.
- **New routing rule kind:** add a `Rule` implementation; the engine does not
  change.
