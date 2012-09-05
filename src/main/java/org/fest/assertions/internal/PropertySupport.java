/*
 * Created on Jun 26, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal;

import static java.lang.String.format;
import static java.util.Collections.*;
import static org.fest.util.Iterables.isNullOrEmpty;
import static org.fest.util.Introspection.getProperty;
import static org.fest.util.Iterables.nonNullElementsIn;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

import org.fest.util.IntrospectionError;
import org.fest.util.VisibleForTesting;

/**
 * Utility methods for properties access.
 * 
 * @author Joel Costigliola
 * @author Alex Ruiz
 * @author Nicolas François
 * @author Florent Biville
 */
public class PropertySupport {

  private static final String SEPARATOR = ".";

  private static final PropertySupport INSTANCE = new PropertySupport();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static PropertySupport instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  JavaBeanDescriptor javaBeanDescriptor = new JavaBeanDescriptor();

  @VisibleForTesting
  PropertySupport() {}

  /**
   * Returns a <code>{@link List}</code> containing the values of the given property name, from the elements of the given
   * <code>{@link Iterable}</code>. If the given {@code Iterable} is empty or {@code null}, this method will return an empty
   * {@code List}. This method supports nested properties (e.g. "address.street.number").
   * @param propertyName the name of the property. It may be a nested property. It is left to the clients to validate for
   *          {@code null} or empty.
   * @param target the given {@code Iterable}.
   * @return an {@code Iterable} containing the values of the given property name, from the elements of the given {@code Iterable}.
   * @throws IntrospectionError if an element in the given {@code Iterable} does not have a property with a matching name.
   */
  public <T> Iterable<T> propertyValues(String propertyName, Class<T> clazz, Iterable<?> target) {
    // ignore null elements as we can't extract a property from a null object
    Iterable<?> cleanedUp = nonNullElementsIn(target);
    if (isNullOrEmpty(cleanedUp)) {
      return emptyList();
    }
    if (isNestedProperty(propertyName)) {
      String firstPropertyName = popPropertyNameFrom(propertyName);
      Iterable<Object> propertyValues = propertyValues(firstPropertyName, Object.class, cleanedUp);
      // extract next sub-property values until reaching the last sub-property
      return propertyValues(nextPropertyNameFrom(propertyName), clazz, propertyValues);
    }
    return simplePropertyValues(propertyName, clazz, cleanedUp);
  }

  /**
   * Static variant of {@link #propertyValue(String, Class, Object)} for syntactic sugar.
   * <p>
   * @param propertyName the name of the property. It may be a nested property. It is left to the clients to validate for
   *          {@code null} or empty.
   * @param target the given object
   * @param clazz type of property
   * @return a the values of the given property name
   * @throws IntrospectionError if the given target does not have a property with a matching name.
   */
  public static <T> T propertyValueOf(String propertyName, Object target, Class<T> clazz) {
    return instance().propertyValue(propertyName, clazz, target);
  }

  private <T> List<T> simplePropertyValues(String propertyName, Class<T> clazz, Iterable<?> target) {
    List<T> propertyValues = new ArrayList<T>();
    for (Object e : target) {
      propertyValues.add(propertyValue(propertyName, clazz, e));
    }
    return unmodifiableList(propertyValues);
  }

  private String popPropertyNameFrom(String propertyNameChain) {
    if (!isNestedProperty(propertyNameChain)) {
      return propertyNameChain;
    }
    return propertyNameChain.substring(0, propertyNameChain.indexOf(SEPARATOR));
  }

  private String nextPropertyNameFrom(String propertyNameChain) {
    if (!isNestedProperty(propertyNameChain)) {
      return "";
    }
    return propertyNameChain.substring(propertyNameChain.indexOf(SEPARATOR) + 1);
  }

  /**
   * <pre>
   * isNestedProperty("address.street"); // true
   * isNestedProperty("address.street.name"); // true
   * isNestedProperty("person"); // false
   * isNestedProperty(".name"); // false
   * isNestedProperty("person."); // false
   * isNestedProperty("person.name."); // false
   * isNestedProperty(".person.name"); // false
   * isNestedProperty("."); // false
   * isNestedProperty(""); // false
   * </pre>
   */
  private boolean isNestedProperty(String propertyName) {
    return propertyName.contains(SEPARATOR) && !propertyName.startsWith(SEPARATOR) && !propertyName.endsWith(SEPARATOR);
  }

  /**
   * Return the value of property from a target object.
   * @param propertyName the name of the property. It may be a nested property. It is left to the clients to validate for
   *          {@code null} or empty.
   * @param target the given object
   * @param clazz type of property
   * @return a the values of the given property name
   * @throws IntrospectionError if the given target does not have a property with a matching name.
   */
  public <T> T propertyValue(String propertyName, Class<T> clazz, Object target) {
    PropertyDescriptor descriptor = getProperty(propertyName, target);
    try {
      return clazz.cast(javaBeanDescriptor.invokeReadMethod(descriptor, target));
    } catch (ClassCastException e) {
      String msg = format("Unable to obtain the value of the property <'%s'> from <%s> - wrong property type specified <%s>",
          propertyName, target, clazz);
      throw new IntrospectionError(msg, e);
    } catch (Throwable unexpected) {
      String msg = format("Unable to obtain the value of the property <'%s'> from <%s>", propertyName, target);
      throw new IntrospectionError(msg, unexpected);
    }
  }

  /**
   * Returns the value of the given property name given target. If the given object is {@code null}, this method will return null.<br>
   * This method supports nested properties (e.g. "address.street.number").
   * @param propertyName the name of the property. It may be a nested property. It is left to the clients to validate for
   *          {@code null} or empty.
   * @param clazz the class of property.
   * @param target the given Object to extract property from.
   * @return the value of the given property name given target.
   * @throws IntrospectionError if target object does not have a property with a matching name.
   */
  public <T> T propertyValueOf(String propertyName, Class<T> clazz, Object target) {
    // returns null if target is null as we can't extract a property from a null object
    if (target == null) {
      return null;
    }

    if (isNestedProperty(propertyName)) {
      String firstPropertyName = popPropertyNameFrom(propertyName);
      Object propertyValue = propertyValue(firstPropertyName, Object.class, target);
      // extract next sub-property values until reaching the last sub-property
      return propertyValueOf(nextPropertyNameFrom(propertyName), clazz, propertyValue);
    }
    return propertyValue(propertyName, clazz, target);
  }

}
