package brokerless.model.transit;

import java.util.UUID;

public interface TransitConstants {
  String OUTBOX_ENDPOINT_PATH = "/brokerless-events/outbox";

  UUID EVENT_ID_ZERO = UUID.fromString("00000000-0000-0000-0000-000000000000");
}
