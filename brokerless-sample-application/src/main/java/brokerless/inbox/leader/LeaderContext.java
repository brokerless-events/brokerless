package com.onegini.oneex.consumer.leader;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.kubernetes.commons.leader.LeaderProperties;
import org.springframework.integration.leader.Context;
import org.springframework.integration.leader.event.OnGrantedEvent;
import org.springframework.integration.leader.event.OnRevokedEvent;
import org.springframework.stereotype.Component;

import com.onegini.oneex.consumer.configuration.condition.ConditionalOnLeaderElectionEnabled;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
      log.warn("Leadership granted for {} role, while service subscribed for {}", event.getRole(), leaderProperties.getRole());
    }
  }

  public void handleLeadershipRevoked(final OnRevokedEvent event) {
    if (roleEqual(event.getRole())) {
      yieldLeadership();
    } else {
      log.warn("Leadership revoked for {} role, while service subscribed for {}", event.getRole(), leaderProperties.getRole());
    }
  }

  private boolean roleEqual(final String role) {
    return StringUtils.isNotBlank(leaderProperties.getRole()) &&
        StringUtils.isNotBlank(role) &&
        leaderProperties.getRole().equals(role);
  }

  void markAsLeader() {
    leader = true;
  }

  void yieldLeadership() {
    leader = false;
  }
}
