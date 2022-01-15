package brokerless.inbox;

import brokerless.model.EventPayload;
import lombok.Value;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Value
public class EventHandlerWrapper {
  Object object;
  Method method;

  public void accept(EventPayload payload) {
    try {
      method.invoke(object, payload);
    } catch (final IllegalAccessException e) {
      throw EventHandlerConfigurationException.noAccess(method);
    } catch (final IllegalArgumentException e) {
      throw EventHandlerConfigurationException.illegalInvocation(method);
    } catch (final InvocationTargetException e) {
      throw new EventHandlerConfigurationException(e.getTargetException());
    }
  }

}
