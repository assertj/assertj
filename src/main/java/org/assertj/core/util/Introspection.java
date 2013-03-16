/*
 * Created on Jun 28, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2012 the original author or authors.
 */
package org.assertj.core.util;

import static java.lang.String.format;
import static java.lang.reflect.Modifier.isPublic;
import static java.util.Locale.ENGLISH;
import static org.assertj.core.util.Preconditions.*;
import static org.assertj.core.util.Strings.quote;

import java.beans.*;
import java.lang.reflect.Method;

/**
 * Utility methods related to <a
 * href="http://java.sun.com/docs/books/tutorial/javabeans/introspection/index.html">JavaBeans Introspection</a>.
 * 
 * @author Alex Ruiz
 */
public final class Introspection {
  /**
   * Returns a {@link PropertyDescriptor} for a property matching the given name in the given object.
   * 
   * @param propertyName the given property name.
   * @param target the given object.
   * @return a {@code PropertyDescriptor} for a property matching the given name in the given object.
   * @throws NullPointerException if the given property name is {@code null}.
   * @throws IllegalArgumentException if the given property name is empty.
   * @throws NullPointerException if the given object is {@code null}.
   * @throws IntrospectionError if a matching property cannot be found or accessed.
   */
  public static PropertyDescriptor getProperty(String propertyName, Object target) {
    checkNotNullOrEmpty(propertyName);
    checkNotNull(target);
    BeanInfo beanInfo = null;
    Class<?> type = target.getClass();
    try {
      beanInfo = Introspector.getBeanInfo(type);
    } catch (Throwable t) {
      throw new IntrospectionError(format("Unable to get BeanInfo for type %s", type.getName()), t);
    }
    for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
      if (propertyName.equals(descriptor.getName())) {
        return descriptor;
      }
    }
    throw new IntrospectionError(propertyNotFoundErrorMessage(propertyName, target));
  }

  private static String propertyNotFoundErrorMessage(String propertyName, Object target) {
    String targetTypeName = target.getClass().getName();
    String property = quote(propertyName);
    Method getter = findGetter(propertyName, target);
    if (getter == null) {
      return format("No getter for property %s in %s", property, targetTypeName);
    }
    if (!isPublic(getter.getModifiers())) {
      return format("No public getter for property %s in %s", property, targetTypeName);
    }
    return format("Unable to find property %s in %s", property, targetTypeName);
  }

  private static Method findGetter(String propertyName, Object target) {
    String capitalized = propertyName.substring(0, 1).toUpperCase(ENGLISH) + propertyName.substring(1);
    // try to find getProperty
    Method getter = findMethod("get" + capitalized, target);
    if (getter != null) {
      return getter;
    }
    // try to find isProperty for boolean properties
    return findMethod("is" + capitalized, target);
  }

  private static Method findMethod(String name, Object target) {
    // TODO walk class hierarchy to check if any superclass declares the method we are looking for.
    try {
      return target.getClass().getDeclaredMethod(name);
    } catch (Throwable t) {
      return null;
    }
  }

  private Introspection() {}
}
