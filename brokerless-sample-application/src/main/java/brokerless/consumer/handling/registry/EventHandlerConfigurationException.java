package brokerless.inbox.handling.registry;

import java.lang.reflect.Method;

import static java.lang.String.format;

public class EventHandlerConfigurationException extends RuntimeException {

  public EventHandlerConfigurationException(String event) {
    super(event);
  }


  public EventHandlerConfigurationException(Throwable throwable) {
    super(throwable);
  }


  public static EventHandlerConfigurationException noAccess(Method method) {
    return new EventHandlerConfigurationException(format(
        "Could not configure handler '%s' because it is not accessible. Handler method should be public.",
        method));
  }


  public static EventHandlerConfigurationException illegalArguments(Method method) {
    return new EventHandlerConfigurationException(format("Could not configure handler '%s' because it does not have the correct arguments.", method);
  }


  public static EventHandlerConfigurationException illegalInvocation(Method method) {
    return new EventHandlerConfigurationException(format(
        "Could not invoke handler '%s' because the provided arguments are not correct.",
        method));
  }


  public static EventHandlerConfigurationException illegalReturnType(Method method) {
    return new EventHandlerConfigurationException(format("Could not configure event handler '%s' because it does not have void return type", method));
  }


  public static EventHandlerConfigurationException illegalArgumentType(Method method) {
    return new EventHandlerConfigurationException(format(
        "Could not configure handler '%s' because it does not have the correct argument types.",
        method));
  }


  public static EventHandlerConfigurationException invocationException(Throwable throwable) {
    return new EventHandlerConfigurationException(throwable);
  }
}
