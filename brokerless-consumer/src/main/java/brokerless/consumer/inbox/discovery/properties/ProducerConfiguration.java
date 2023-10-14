package brokerless.consumer.inbox.discovery.properties;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProducerConfiguration {

  private String name;
  private String baseUrl;

}
