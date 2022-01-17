package brokerless.model.transit;

import java.util.Set;
import java.util.UUID;

public record GetOutboxEventsRequest(Set<String> eventTypes, UUID fromCursorExclusive) {

}
