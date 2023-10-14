package brokerless.model;

public @interface BrokerlessEvent {
  int versionMajor() default 1;
  int versionMinor() default 0;
}
