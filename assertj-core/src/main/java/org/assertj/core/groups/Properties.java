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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.groups;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.util.ArrayWrapperList.wrap;
import static org.assertj.core.util.Preconditions.checkArgument;

import java.util.List;

import org.assertj.core.util.VisibleForTesting;
import org.assertj.core.util.introspection.IntrospectionError;
import org.assertj.core.util.introspection.PropertySupport;

/**
 * Extracts the values of a specified property from the elements of a given <code>{@link Iterable}</code> or array.
 * 
 * @author Yvonne Wang
 * @author Mikhail Mazursky
 * @author Joel Costigliola
 * @author Florent Biville
 * @author Olivier Michallat
 */
public class Properties<T> {

  @VisibleForTesting
  final String propertyName;
  final Class<T> propertyType;

  @VisibleForTesting
  PropertySupport propertySupport = PropertySupport.instance();

  /**
   * Creates a new <code>{@link Properties}</code>.
   * @param <T> the type of value to extract.
   * @param propertyName the name of the property to be read from the elements of a {@code Iterable}. It may be a nested
   *          property (e.g. "address.street.number").
   * @param propertyType the type of property to extract
   * @return the created {@code Properties}.
   * @throws NullPointerException if the given property name is {@code null}.
   * @throws IllegalArgumentException if the given property name is empty.
   */
  public static <T> Properties<T> extractProperty(String propertyName, Class<T> propertyType) {
    checkIsNotNullOrEmpty(propertyName);
    return new Properties<>(propertyName, propertyType);
  }

  /**
   * Creates a new <code>{@link Properties} with given propertyName and Object as property type.</code>.
   * 
   * @param propertyName the name of the property to be read from the elements of a {@code Iterable}. It may be a nested
   *          property (e.g. "address.street.number").
   * @return the created {@code Properties}.
   * @throws NullPointerException if the given property name is {@code null}.
   * @throws IllegalArgumentException if the given property name is empty.
   */
  public static Properties<Object> extractProperty(String propertyName) {
    return extractProperty(propertyName, Object.class);
  }

  private static void checkIsNotNullOrEmpty(String propertyName) {
    requireNonNull(propertyName, "The name of the property to read should not be null");
    checkArgument(propertyName.length() > 0, "The name of the property to read should not be empty");
  }

  @VisibleForTesting
  Properties(String propertyName, Class<T> propertyType) {
    this.propertyName = propertyName;
    this.propertyType = propertyType;
  }

  /**
   * Specifies the target type of an instance that was previously created with {@link #extractProperty(String)}.
   * <p>
   * This is so that you can write:
   * <pre><code class='java'> extractProperty("name").ofType(String.class).from(fellowshipOfTheRing);</code></pre>
   * 
   * instead of:
   * <pre><code class='java'> extractProperty("name", String.class).from(fellowshipOfTheRing);</code></pre>
   *
   * @param <U> the type of value to extract.
   * @param propertyType the type of property to extract.
   * @return a new {@code Properties} with the given type.
   */
  public <U> Properties<U> ofType(Class<U> propertyType) {
    return extractProperty(this.propertyName, propertyType);
  }

  /**
   * Extracts the values of the property (specified previously in <code>{@link #extractProperty(String)}</code>) from the elements
   * of the given <code>{@link Iterable}</code>.
   * @param c the given {@code Iterable}.
   * @return the values of the previously specified property extracted from the given {@code Iterable}.
   * @throws IntrospectionError if an element in the given {@code Iterable} does not have a property with a matching name.
   */
  public List<T> from(Iterable<?> c) {
    return propertySupport.propertyValues(propertyName, propertyType, c);
  }

  /**
   * Extracts the values of the property (specified previously in <code>{@link #extractProperty(String)}</code>) from the elements
   * of the given array.
   * @param array the given array.
   * @return the values of the previously specified property extracted from the given array.
   * @throws IntrospectionError if an element in the given array does not have a property with a matching name.
   */
  public List<T> from(Object[] array) {
    return propertySupport.propertyValues(propertyName, propertyType, wrap(array));
  }
}
