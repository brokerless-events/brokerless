# Architecture Decision Records

## Explicit consumer subscription - feature not planned

Explicit consumer subscriptions is a feature where each consumer upon startup subscribes for a list of consumed events at producers. In the outbox the producer keeps separate entry of every event for every consumer. This is an alternative design to one where consumers query producers in a stateless way, events from outbox are returned based on the cursor. 

Pros of explicit consumer subscriptions:
 - producer knows which events have been already received by each producer based on the cursor and therefore can prune those events from the outbox table

Cons of explicit consumer subscriptions:
 - Increased complexity: additional subscribe call on consumer startup, subscriptions persistence, necessity to keep track of published event types 

In the alternative design of stateless outbox querying the events can be deleted from the outbox after defined retention period. This seems to be good enough.
