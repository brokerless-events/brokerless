package brokerless.producer.outbox.persistence;

import brokerless.model.EventMetadata;
import brokerless.model.EventPayload;
import brokerless.model.transit.TransitedEventMessage;
import brokerless.producer.publication.EventProductionTracing;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface OutboxRepositoryClient {

    void store(EventPayload payload, EventMetadata metadata, EventProductionTracing tracing);

    List<TransitedEventMessage> read(Set<String> types, UUID afterCursor);

}
