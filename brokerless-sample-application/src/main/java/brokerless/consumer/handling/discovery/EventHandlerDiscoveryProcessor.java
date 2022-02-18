package brokerless.consumer.handling.discovery;

import brokerless.consumer.handling.BrokerlessEventHandler;
import brokerless.consumer.handling.registry.EventHandlerRegistry;
import brokerless.model.EventMetadata;
import brokerless.model.EventPayload;
import brokerless.model.EventTracing;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.TriConsumer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.MethodFilter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static brokerless.consumer.handling.registry.EventHandlerConfigurationException.*;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

@Configuration
public class EventHandlerDiscoveryProcessor implements BeanPostProcessor {

  private static final MethodFilter hasAnnotation = method -> findAnnotation(method, BrokerlessEventHandler.class) != null;

  private final EventHandlerRegistry registry;

  public EventHandlerDiscoveryProcessor(EventHandlerRegistry registry) {
    this.registry = registry;
  }


  @SuppressWarnings("unchecked")
  @Override
  public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
    ReflectionUtils.doWithMethods(bean.getClass(), method -> {
      if (!method.canAccess(bean)) {
        throw noAccess(method);
      }
      if (!Void.TYPE.isAssignableFrom(method.getReturnType())) {
        throw illegalReturnType(method);
      }
      if (method.getParameterCount() == 1 && EventPayload.class.isAssignableFrom(method.getParameterTypes()[0])) {
        registry.register((Class<? extends EventPayload>) method.getParameterTypes()[0], new OneArgumentInvoker<>(bean, method));
      } else if (method.getParameterCount() == 2 &&
          EventPayload.class.isAssignableFrom(method.getParameterTypes()[0]) &&
          EventMetadata.class.isAssignableFrom(method.getParameterTypes()[1])) {
        registry.register((Class<? extends EventPayload>) method.getParameterTypes()[0], new TwoArgumentsInvoker<>(bean, method));
      }  else if (method.getParameterCount() == 3 &&
          EventPayload.class.isAssignableFrom(method.getParameterTypes()[0]) &&
          EventMetadata.class.isAssignableFrom(method.getParameterTypes()[1]) &&
          EventTracing.class.isAssignableFrom(method.getParameterTypes()[2])) {
        registry.register((Class<? extends EventPayload>) method.getParameterTypes()[0], new ThreeArgumentsInvoker<>(bean, method));
      } else {
        throw illegalArguments(method);
      }
    }, hasAnnotation);
    return bean;
  }

  @RequiredArgsConstructor
  private abstract static class Invoker<T> implements TriConsumer<T, EventMetadata, EventTracing> {
    protected final Object bean;
    protected final Method method;

    @Override
    public void accept(final T event, final EventMetadata metadata, final EventTracing tracing) {
      try {
        doInvoke(event, metadata, tracing);
      } catch (final IllegalAccessException e ) {
        throw noAccess(method);
      } catch (final IllegalArgumentException e) {
        throw illegalArguments(method);
      } catch (final InvocationTargetException e) {
        throw invocationException(e.getTargetException());
      }
    }

    protected abstract void doInvoke(final T event, EventMetadata metadata, EventTracing tracing) throws IllegalAccessException, InvocationTargetException;
  }


  private static class OneArgumentInvoker<T> extends Invoker<T> {

    private OneArgumentInvoker(final Object bean, final Method method) {
      super(bean, method);
    }

    protected void doInvoke(final T event, EventMetadata metadata, EventTracing tracing) throws IllegalAccessException, InvocationTargetException {
      method.invoke(bean, event);
    }
  }

  private static class TwoArgumentsInvoker<T> extends Invoker<T> {
    public TwoArgumentsInvoker(final Object bean, final Method method) {
      super(bean, method);
    }

    protected void doInvoke(final T event, EventMetadata metadata, EventTracing tracing) throws IllegalAccessException, InvocationTargetException {
      method.invoke(bean, event, metadata);
    }
  }

  private static class ThreeArgumentsInvoker<T> extends Invoker<T> {
    public ThreeArgumentsInvoker(final Object bean, final Method method) {
      super(bean, method);
    }

    protected void doInvoke(final T event, EventMetadata metadata, EventTracing tracing) throws IllegalAccessException, InvocationTargetException {
      method.invoke(bean, event, metadata, tracing);
    }
  }
}
