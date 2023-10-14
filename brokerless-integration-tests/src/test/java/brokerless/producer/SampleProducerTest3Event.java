package brokerless.producer;

import brokerless.model.EventPayload;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class SampleProducerTest3Event implements EventPayload {
  String data3;
}
