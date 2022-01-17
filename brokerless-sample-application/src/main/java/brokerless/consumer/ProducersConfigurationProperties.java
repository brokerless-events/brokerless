package brokerless.inbox;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "borkerless.producers")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProducersConfigurationProperties {

  private List<ProducerConfiguration> producers;

}
