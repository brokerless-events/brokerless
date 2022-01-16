package com.onegini.oneex.consumer.leader;

import java.util.Objects;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.kubernetes.commons.PodUtils;
import org.springframework.context.event.EventListener;
import org.springframework.integration.leader.event.OnGrantedEvent;
import org.springframework.integration.leader.event.OnRevokedEvent;
import org.springframework.stereotype.Service;

import com.onegini.oneex.consumer.configuration.condition.ConditionalOnLeaderElectionEnabled;
import io.fabric8.kubernetes.api.model.Pod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnLeaderElectionEnabled
public class LeaderElectionHandler {

  static final String LOCAL_NODE_NAME = "Local-Node";
  static final String K8S_NODE_NAME = "K8S-Node";

  private final ObjectProvider<PodUtils<Pod>> podUtilsProvider;
  private final LeaderContext leaderContext;

  @EventListener
  public void handleEvent(final OnGrantedEvent event) {
    log.info("OneEx Consumer leadership granted, node name {}, ", getNodeName());
    leaderContext.handleLeadershipGranted(event);
  }

  @EventListener
  public void handleEvent(final OnRevokedEvent event) {
    log.info("OneEx Consumer leadership revoked, node name {}, ", getNodeName());
    leaderContext.handleLeadershipRevoked(event);
  }

  private String getNodeName() {
    return isRunningInKubernetes() ? getPodName() : LOCAL_NODE_NAME;
  }

  private boolean isRunningInKubernetes() {
    return podUtilsProvider.getIfAvailable() != null && podUtilsProvider.getIfAvailable().isInsideKubernetes();
  }

  private String getPodName() {
    final Pod pod = Objects.requireNonNull(podUtilsProvider.getIfAvailable()).currentPod().get();
    if (pod.getMetadata() != null) {
      return pod.getMetadata().getName();
    } else {
      return K8S_NODE_NAME;
    }
  }
}
