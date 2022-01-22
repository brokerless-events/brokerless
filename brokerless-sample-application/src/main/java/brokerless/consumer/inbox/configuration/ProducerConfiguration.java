package brokerless.consumer.inbox.configuration;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class ProducerConfiguration {

  private String name;
  private String baseUrl;

}
