package org.assertj.core.api;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.assertj.core.groups.Properties;

/** Collects error messages of all AssertionErrors thrown by the proxied method. */
public class ErrorCollector implements MethodInterceptor {

  private final List<Throwable> errors = new ArrayList<Throwable>();

  @Override
  public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
    try {
      proxy.invokeSuper(obj, args);
    } catch (AssertionError e) {
      errors.add(e);
    }
    return obj;
  }

  public List<Throwable> errors() {
    return Collections.unmodifiableList(errors);
  }
}
