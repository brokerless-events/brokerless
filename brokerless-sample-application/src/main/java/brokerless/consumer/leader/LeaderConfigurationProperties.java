package brokerless.consumer.leader;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.kubernetes.commons.leader.LeaderProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnLeaderElectionEnabled
@EnableConfigurationProperties(LeaderProperties.class)
public class LeaderConfigurationProperties {
}
