package brokerless.outbox;

import brokerless.model.EventMetadata;
import brokerless.model.EventPayload;
import brokerless.model.transmission.SerializedEventMessage;

import java.util.List;
import java.util.Set;

public interface OutboxClient {

    void store(EventPayload payload, EventMetadata metadata, EventProductionTracing tracing);

    List<SerializedEventMessage> read(Set<String> types, long fromCursorExclusive);

}
