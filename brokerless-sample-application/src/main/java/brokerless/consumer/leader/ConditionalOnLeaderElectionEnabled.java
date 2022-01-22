package brokerless.consumer.leader;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Documented
@ConditionalOnProperty(value = "brokerless.leader.enabled", havingValue = "true")
public @interface ConditionalOnLeaderElectionEnabled {
}
