package brokerless.inbox.handling;

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


  public static EventHandlerConfigurationException illegalArguments(Method method, int argsCount, Class argClass) {
    return new EventHandlerConfigurationException(format(
        "Could not configure handler '%s' because it does not have the correct arguments. This handler should have %d argument that is assignable to class %s",
        method, argsCount, argClass.getSimpleName()));
  }


  public static EventHandlerConfigurationException illegalInvocation(Method method) {
    return new EventHandlerConfigurationException(format(
        "Could not invoke handler '%s' because the provided arguments are not correct.",
        method));
  }


  public static EventHandlerConfigurationException illegalReturnType(Method method, Class<?> type) {
    return new EventHandlerConfigurationException(format(
        "Could not configure handler '%s' because it does not have the correct return type assignable to %s",
        method, type));
  }


  public static EventHandlerConfigurationException illegalArgumentType(Method method) {
    return new EventHandlerConfigurationException(format(
        "Could not configure handler '%s' because it does not have the correct argument types.",
        method));
  }
}
