package brokerless.producer.publication;

import brokerless.model.EventPayload;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class SampleProducerTest3Event implements EventPayload {
  String data3;
}
