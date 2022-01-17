package brokerless.producer;

import brokerless.model.EventMetadata;
import brokerless.model.EventPayload;
import brokerless.model.transit.SerializedEventMessage;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface OutboxClient {

    void store(EventPayload payload, EventMetadata metadata, EventProductionTracing tracing);

    List<SerializedEventMessage> read(Set<String> types, UUID fromCursorExclusive);

}
