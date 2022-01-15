package brokerless.outbox;

import java.util.Set;

public record OutboxRequest(Set<String> eventTypes, long fromCursorExclusive) {

}
