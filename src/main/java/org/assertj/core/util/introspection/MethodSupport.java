package org.assertj.core.util.introspection;

import static java.lang.String.format;

import java.lang.reflect.Method;

import org.assertj.core.util.Preconditions;

/**
 * Utillity class for reflective method invocation.
 * 
 * @author Michał Piotrkowski
 * 
 */
public class MethodSupport {

  private static final String METHOD_HAS_NO_RETURN_VALUE = "Method with name '%s' in class %s.class has to return value!";
  private static final String METHOD_NOT_FOUND = "Can't find method with name '%s' in class %s.class. Make sure public method exist and accepts no arguments!";

  public static Object methodResultFor(Object item, String methodName) {

    Preconditions.checkNotNull(item, "Object instance can not be null!");
    Preconditions.checkNotNullOrEmpty(methodName, "Method name can not be empty!");

    Method method = findMethod(methodName, item.getClass());
    return invokeMethod(item, method);

  }

  private static Object invokeMethod(Object item, Method method) {

    try {

      return method.invoke(item);

    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  private static Method findMethod(String methodName, Class<? extends Object> itemClass) {

    try {

      Method method = itemClass.getMethod(methodName, new Class<?>[0]);
      assertHasReturnType(itemClass, method);

      return method;

    } catch (SecurityException e) {
      throw prepareMethodNotFoundException(methodName, itemClass, e);
    } catch (NoSuchMethodException e) {
      throw prepareMethodNotFoundException(methodName, itemClass, e);
    }
  }

  private static IllegalArgumentException prepareMethodNotFoundException(String methodName,
      Class<? extends Object> itemClass, Exception cause) {

    String message = format(METHOD_NOT_FOUND, methodName, itemClass.getSimpleName());
    return new IllegalArgumentException(message, cause);
  }

  private static void assertHasReturnType(Class<? extends Object> itemClass, Method method) {
    if (Void.TYPE.equals(method.getReturnType())) {
      String message = format(METHOD_HAS_NO_RETURN_VALUE, method.getName(), itemClass.getSimpleName());
      throw new IllegalArgumentException(message);
    }
  }

}
