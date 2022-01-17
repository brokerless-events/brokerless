package com.onegini.oneex.consumer.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.kubernetes.commons.leader.LeaderProperties;
import org.springframework.context.annotation.Configuration;

import com.onegini.oneex.consumer.configuration.condition.ConditionalOnLeaderElectionEnabled;

@Configuration
@ConditionalOnLeaderElectionEnabled
@EnableConfigurationProperties(LeaderProperties.class)
public class LeaderConfigurationProperties {
}
