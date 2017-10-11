/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.util.introspection;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static org.assertj.core.util.IterableUtil.isNullOrEmpty;
import static org.assertj.core.util.Preconditions.checkArgument;
import static org.assertj.core.util.introspection.Introspection.getPropertyGetter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.VisibleForTesting;

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
   * 
   * @return the singleton instance of this class.
   */
  public static PropertySupport instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  PropertySupport() {
  }

  /**
   * Returns a <code>{@link List}</code> containing the values of the given property name, from the elements of the
   * given <code>{@link Iterable}</code>. If the given {@code Iterable} is empty or {@code null}, this method will
   * return an empty {@code List}. This method supports nested properties (e.g. "address.street.number").
   * 
   * @param <T> the type of the extracted elements.
   * @param propertyName the name of the property. It may be a nested property. It is left to the clients to validate
   *          for {@code null} or empty.
   * @param clazz type of property
   * @param target the given {@code Iterable}.
   * @return an {@code Iterable} containing the values of the given property name, from the elements of the given
   *         {@code Iterable}.
   * @throws IntrospectionError if an element in the given {@code Iterable} does not have a property with a matching
   *           name.
   */
  public <T> List<T> propertyValues(String propertyName, Class<T> clazz, Iterable<?> target) {
    if (isNullOrEmpty(target)) {
      return emptyList();
    }
    if (isNestedProperty(propertyName)) {
      String firstPropertyName = popPropertyNameFrom(propertyName);
      Iterable<Object> propertyValues = propertyValues(firstPropertyName, Object.class, target);
      // extract next sub-property values until reaching the last sub-property
      return propertyValues(nextPropertyNameFrom(propertyName), clazz, propertyValues);
    }
    return simplePropertyValues(propertyName, clazz, target);
  }

  /**
   * Static variant of {@link #propertyValueOf(String, Class, Object)} for syntactic sugar.
   * 
   * @param <T> the type of the extracted elements.
   * @param propertyName the name of the property. It may be a nested property. It is left to the clients to validate
   *          for {@code null} or empty.
   * @param target the given object
   * @param clazz type of property
   * @return a the values of the given property name
   * @throws IntrospectionError if the given target does not have a property with a matching name.
   */
  public static <T> T propertyValueOf(String propertyName, Object target, Class<T> clazz) {
    return instance().propertyValueOf(propertyName, clazz, target);
  }

  private <T> List<T> simplePropertyValues(String propertyName, Class<T> clazz, Iterable<?> target) {
    List<T> propertyValues = new ArrayList<>();
    for (Object e : target) {
      propertyValues.add(e == null ? null : propertyValue(propertyName, clazz, e));
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
   * <pre><code class='java'> isNestedProperty(&quot;address.street&quot;); // true
   * isNestedProperty(&quot;address.street.name&quot;); // true
   * isNestedProperty(&quot;person&quot;); // false
   * isNestedProperty(&quot;.name&quot;); // false
   * isNestedProperty(&quot;person.&quot;); // false
   * isNestedProperty(&quot;person.name.&quot;); // false
   * isNestedProperty(&quot;.person.name&quot;); // false
   * isNestedProperty(&quot;.&quot;); // false
   * isNestedProperty(&quot;&quot;); // false</code></pre>
   */
  private boolean isNestedProperty(String propertyName) {
    return propertyName.contains(SEPARATOR) && !propertyName.startsWith(SEPARATOR) && !propertyName.endsWith(SEPARATOR);
  }

  /**
   * Return the value of a simple property from a target object.
   * <p>
   * This only works for simple property, nested property are not supported ! use
   * {@link #propertyValueOf(String, Class, Object)}
   * 
   * @param <T> the type of the extracted value.
   * @param propertyName the name of the property. It may be a nested property. It is left to the clients to validate
   *          for {@code null} or empty.
   * @param target the given object
   * @param clazz type of property
   * @return a the values of the given property name
   * @throws IntrospectionError if the given target does not have a property with a matching name.
   */
  @SuppressWarnings("unchecked")
  public <T> T propertyValue(String propertyName, Class<T> clazz, Object target) {
    Method getter = getPropertyGetter(propertyName, target);
    try {
      return (T) getter.invoke(target);
    } catch (ClassCastException e) {
      String msg = format("Unable to obtain the value of the property <'%s'> from <%s> - wrong property type specified <%s>",
                          propertyName, target, clazz);
      throw new IntrospectionError(msg, e);
    } catch (Exception unexpected) {
      String msg = format("Unable to obtain the value of the property <'%s'> from <%s>", propertyName, target);
      throw new IntrospectionError(msg, unexpected);
    }
  }

  /**
   * Returns the value of the given property name given target. If the given object is {@code null}, this method will
   * return null.<br>
   * This method supports nested properties (e.g. "address.street.number").
   * 
   * @param <T> the type of the extracted value.
   * @param propertyName the name of the property. It may be a nested property. It is left to the clients to validate
   *          for {@code null} or empty.
   * @param clazz the class of property.
   * @param target the given Object to extract property from.
   * @return the value of the given property name given target.
   * @throws IntrospectionError if target object does not have a property with a matching name.
   * @throws IllegalArgumentException if propertyName is null.
   */
  public <T> T propertyValueOf(String propertyName, Class<T> clazz, Object target) {
    checkArgument(propertyName != null, "the property name should not be null.");
    // returns null if target is null as we can't extract a property from a null object
    // but don't want to raise an exception if we were looking at a nested property
    if (target == null) return null;

    if (isNestedProperty(propertyName)) {
      String firstPropertyName = popPropertyNameFrom(propertyName);
      Object propertyValue = propertyValue(firstPropertyName, Object.class, target);
      // extract next sub-property values until reaching the last sub-property
      return propertyValueOf(nextPropertyNameFrom(propertyName), clazz, propertyValue);
    }
    return propertyValue(propertyName, clazz, target);
  }

  /**
   * Returns a <code>{@link List}</code> containing the values of the given property name, from the elements of the
   * given <code>{@link Iterable}</code>. If the given {@code Iterable} is empty or {@code null}, this method will
   * return an empty {@code List}. This method supports nested properties (e.g. "address.street.number").
   * 
   * @param fieldOrPropertyName the name of the property. It may be a nested property. It is left to the clients to validate
   *          for {@code null} or empty.
   * @param target the given {@code Iterable}.
   * @return an {@code Iterable} containing the values of the given property name, from the elements of the given
   *         {@code Iterable}.
   * @throws IntrospectionError if an element in the given {@code Iterable} does not have a property with a matching
   *           name.
   */
  public List<Object> propertyValues(String fieldOrPropertyName, Iterable<?> target) {
    return propertyValues(fieldOrPropertyName, Object.class, target);
  }

  public boolean publicGetterExistsFor(String fieldName, Object actual) {
	try {
	  getPropertyGetter(fieldName, actual);
    } catch (IntrospectionError e) {
      return false;
    }
	return true;
  }

}
