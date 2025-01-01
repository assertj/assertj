/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.util.introspection;

import static java.lang.String.format;
import static java.lang.reflect.Modifier.isPublic;
import static java.util.Locale.ENGLISH;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.util.Preconditions.checkNotNullOrEmpty;
import static org.assertj.core.util.Strings.quote;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.assertj.core.configuration.ConfigurationProvider;
import org.assertj.core.util.VisibleForTesting;

/**
 * Utility methods related to <a
 * href="http://java.sun.com/docs/books/tutorial/javabeans/introspection/index.html">JavaBeans Introspection</a>.
 *
 * @author Alex Ruiz
 */
public final class Introspection {

  // We want to cache negative results (i.e. absence of methods) to avoid same overhead on subsequent lookups
  // However ConcurrentHashMap does not permit nulls - Optional allows caching of 'missing' values
  private static final Map<MethodKey, Optional<Method>> METHOD_CACHE = new ConcurrentHashMap<>();

  // set false by default to follow the principle of least surprise as usual property getter are getX() isX(), not x().
  private static boolean bareNamePropertyMethods = false;

  /**
   * Returns the getter {@link Method} for a property matching the given name in the given object.
   *
   * @param propertyName the given property name.
   * @param target       the given object.
   * @return the getter {@code Method} for a property matching the given name in the given object.
   * @throws NullPointerException     if the given property name is {@code null}.
   * @throws IllegalArgumentException if the given property name is empty.
   * @throws NullPointerException     if the given object is {@code null}.
   * @throws IntrospectionError       if the getter for the matching property cannot be found or accessed.
   */
  public static Method getPropertyGetter(String propertyName, Object target) {
    checkNotNullOrEmpty(propertyName);
    requireNonNull(target);
    Method getter = findGetter(propertyName, target);
    if (getter == null) {
      throw new IntrospectionError(propertyNotFoundErrorMessage("No getter for property %s in %s", propertyName, target));
    }
    if (!isPublic(getter.getModifiers())) {
      throw new IntrospectionError(propertyNotFoundErrorMessage("No public getter for property %s in %s", propertyName, target));
    }
    try {
      // force access for static class with public getter
      getter.setAccessible(true);
      getter.invoke(target);
    } catch (InvocationTargetException ex) {
      String message = format("Unable to invoke getter %s in %s, exception: %s",
                              getter.getName(), target.getClass().getSimpleName(), ex.getTargetException());
      throw new IntrospectionError(message, ex, ex.getTargetException());
    } catch (Exception t) {
      throw new IntrospectionError(propertyNotFoundErrorMessage("Unable to find property %s in %s", propertyName, target), t);
    }
    return getter;
  }

  public static void setExtractBareNamePropertyMethods(boolean bareNamePropertyMethods) {
    ConfigurationProvider.loadRegisteredConfiguration();
    Introspection.bareNamePropertyMethods = bareNamePropertyMethods;
  }

  @VisibleForTesting
  public static boolean canExtractBareNamePropertyMethods() {
    return bareNamePropertyMethods;
  }

  private static String propertyNotFoundErrorMessage(String message, String propertyName, Object target) {
    String targetTypeName = target.getClass().getName();
    String property = quote(propertyName);
    return format(message, property, targetTypeName);
  }

  private static Method findGetter(String propertyName, Object target) {
    String capitalized = propertyName.substring(0, 1).toUpperCase(ENGLISH) + propertyName.substring(1);
    // try to find getProperty
    Method getter = findMethod("get" + capitalized, target);
    if (isValidGetter(getter)) return getter;
    if (bareNamePropertyMethods || isRecordInstance(target)) {
      // try to find bare name property
      getter = findMethod(propertyName, target);
      if (isValidGetter(getter)) return getter;
    }
    // try to find isProperty for boolean properties
    Method isAccessor = findMethod("is" + capitalized, target);
    return isValidGetter(isAccessor) ? isAccessor : null;
  }

  private static boolean isValidGetter(Method method) {
    return method != null && !Modifier.isStatic(method.getModifiers()) && !Void.TYPE.equals(method.getReturnType());
  }

  private static boolean isRecordInstance(Object target) {
    try {
      return Class.forName("java.lang.Record").isInstance(target);
    } catch (ClassNotFoundException e) {
      return false;
    }
  }

  private static Method findMethod(String name, Object target) {
    final MethodKey methodKey = new MethodKey(name, target.getClass());
    return METHOD_CACHE.computeIfAbsent(methodKey, Introspection::findMethodByKey).orElse(null);
  }

  private static Optional<Method> findMethodByKey(MethodKey key) {
    // try public methods only
    Class<?> clazz = key.clazz;
    try {
      return Optional.of(clazz.getMethod(key.name));
    } catch (NoSuchMethodException | SecurityException ignored) {}
    // search all methods
    while (clazz != null) {
      try {
        return Optional.of(clazz.getDeclaredMethod(key.name));
      } catch (NoSuchMethodException | SecurityException ignored) {}
      clazz = clazz.getSuperclass();
    }
    return Optional.empty();
  }

  private static final class MethodKey {
    private final String name;
    private final Class<?> clazz;

    private MethodKey(final String name, final Class<?> clazz) {
      this.name = name;
      this.clazz = clazz;
    }

    @Override
    public boolean equals(final Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      final MethodKey methodKey = (MethodKey) o;
      return Objects.equals(name, methodKey.name) && Objects.equals(clazz, methodKey.clazz);
    }

    @Override
    public int hashCode() {
      return Objects.hash(name, clazz);
    }
  }

  private Introspection() {}
}
