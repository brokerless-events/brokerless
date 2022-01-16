package brokerless.outbox;

import java.util.Set;
import java.util.UUID;

public record OutboxRequest(Set<String> eventTypes, UUID fromCursorExclusive) {

}
