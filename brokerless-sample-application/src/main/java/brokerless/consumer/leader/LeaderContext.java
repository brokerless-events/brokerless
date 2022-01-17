package brokerless.consumer.leader;

import org.springframework.cloud.kubernetes.commons.leader.LeaderProperties;
import org.springframework.integration.leader.Context;
import org.springframework.integration.leader.event.OnGrantedEvent;
import org.springframework.integration.leader.event.OnRevokedEvent;
import org.springframework.stereotype.Component;

import com.onegini.oneex.consumer.configuration.condition.ConditionalOnLeaderElectionEnabled;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnLeaderElectionEnabled
public class LeaderContext implements Context {

  private final LeaderProperties leaderProperties;
  private boolean leader;

  @Override
  public boolean isLeader() {
    return leader;
  }

  public String getConfigMapName() {
    return leaderProperties.getConfigMapName();
  }

  public void handleLeadershipGranted(final OnGrantedEvent event) {
    if (roleEqual(event.getRole())) {
      markAsLeader();
    } else {
      log.warn("Leadership granted for {} role, while subscribed for role {}", event.getRole(), leaderProperties.getRole());
    }
  }

  public void handleLeadershipRevoked(final OnRevokedEvent event) {
    if (roleEqual(event.getRole())) {
      yieldLeadership();
    } else {
      log.warn("Leadership revoked for {} role, while subscribed for role {}", event.getRole(), leaderProperties.getRole());
    }
  }

  private boolean roleEqual(final String role) {
    return isNotBlank(leaderProperties.getRole()) && leaderProperties.getRole().equals(role);
  }

  void markAsLeader() {
    leader = true;
  }

  void yieldLeadership() {
    leader = false;
  }
}
