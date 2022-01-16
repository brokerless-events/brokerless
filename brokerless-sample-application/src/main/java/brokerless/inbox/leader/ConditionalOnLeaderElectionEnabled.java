package com.onegini.oneex.consumer.configuration.condition;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Target(TYPE)
@Retention(RUNTIME)
@Documented
@ConditionalOnProperty(value = "oneex.leader.enabled", havingValue = "true")
public @interface ConditionalOnLeaderElectionEnabled {
}
