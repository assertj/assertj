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
package org.assertj.core.api.filter;

import static java.util.Objects.deepEquals;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Preconditions.checkArgument;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.assertj.core.util.Strings;
import org.assertj.core.util.VisibleForTesting;
import org.assertj.core.util.introspection.IntrospectionError;
import org.assertj.core.util.introspection.PropertyOrFieldSupport;

/**
 * Filters the elements of a given <code>{@link Iterable}</code> or array according to the specified filter criteria.
 * <p>
 * Filter criteria can be expressed either by a {@link Condition} or a pseudo filter language on elements properties.
 * <p>
 * With fluent filter language on element properties/fields :
 * <pre><code class='java'> assertThat(filter(players).with("pointsPerGame").greaterThan(20)
 *                           .and("assistsPerGame").greaterThan(7).get())
 *                           .containsOnly(james, rose);</code></pre>
 *
 * With {@link Condition} :
 * <pre><code class='java'> List&lt;Player&gt; players = ...;
 *
 * Condition&lt;Player&gt; potentialMVP = new Condition&lt;Player&gt;("is a possible MVP"){
 *   public boolean matches(Player player) {
 *     return player.getPointsPerGame() &gt; 20 &amp;&amp; player.getAssistsPerGame() &gt; 7;
 *   };
 * };
 *
 * // use filter static method to build Filters
 * assertThat(filter(players).being(potentialMVP).get()).containsOnly(james, rose);</code></pre>
 *
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 *
 * @param <E> the type of element of group to filter.
 */
public class Filters<E> {

  // initialIterable is never modified, it represents the group before any filters have been performed
  @VisibleForTesting
  final Iterable<E> initialIterable;
  List<E> filteredIterable;

  private static final PropertyOrFieldSupport PROPERTY_OR_FIELD_SUPPORT = PropertyOrFieldSupport.EXTRACTION;

  /**
   * The name of the property used for filtering.
   */
  private String propertyOrFieldNameToFilterOn;

  /**
   * Creates a new <code>{@link Filters}</code> with the {@link Iterable} to filter.
   * <p>
   * Chain this call to express filter criteria either by a {@link Condition} or a pseudo filter language on elements
   * properties or fields (reading private fields is supported but disabled by calling
   * {@link Assertions#setAllowExtractingPrivateFields(boolean) Assertions.setAllowExtractingPrivateFields(false)}.
   * <p>
   * Note that the given {@link Iterable} is not modified, the filters are performed on a copy.
   * <p>
   * With fluent filter language on element properties/fields :
   * <pre><code class='java'> List&lt;Player&gt; players = ...;
   *
   * assertThat(filter(players).with("pointsPerGame").greaterThan(20)
   *                           .and("assistsPerGame").greaterThan(7).get())
   *                           .containsOnly(james, rose);</code></pre>
   *
   * With {@link Condition} :
   * <pre><code class='java'>
   *   public boolean matches(Player player) {
   *     return player.getPointsPerGame() &gt; 20 &amp;&amp; player.getAssistsPerGame() &gt; 7;
   *   };
   * };
   *
   * // use filter static method to build Filters
   * assertThat(filter(players).being(potentialMVP).get()).containsOnly(james, rose);</code></pre>
   *
   * @param <E> the iterable elements type.
   * @param iterable the {@code Iterable} to filter.
   * @return the created <code>{@link Filters}</code>.
   * @throws NullPointerException if the given iterable is {@code null}.
   */
  public static <E> Filters<E> filter(Iterable<E> iterable) {
    return new Filters<>(requireNonNull(iterable, "The iterable to filter should not be null"));
  }

  /**
   * Creates a new <code>{@link Filters}</code> with the array to filter.
   * <p>
   * Chain this call to express filter criteria either by a {@link Condition} or a pseudo filter language on elements
   * properties.
   * <p>
   * Note that the given array is not modified, the filters are performed on an {@link Iterable} copy of the array.
   * <p>
   * With {@link Condition} :
   * <pre><code class='java'> Player[] players = ...;
   *
   * assertThat(filter(players).with("pointsPerGame").greaterThan(20)
   *                           .and("assistsPerGame").greaterThan(7).get())
   *                           .containsOnly(james, rose);</code></pre>
   *
   * With {@link Condition} :
   * <pre><code class='java'> Condition&lt;Player&gt; potentialMVP = new Condition&lt;Player&gt;("is a possible MVP"){
   *   public boolean matches(Player player) {
   *     return player.getPointsPerGame() &gt; 20 &amp;&amp; player.getAssistsPerGame() &gt; 7;
   *   };
   * };
   *
   * // use filter static method to build Filters
   * assertThat(filter(players).being(potentialMVP).get()).containsOnly(james, rose);</code></pre>
   *
   * @param <E> the array elements type.
   * @param array the array to filter.
   * @return the created <code>{@link Filters}</code>.
   * @throws NullPointerException if the given array is {@code null}.
   */
  public static <E> Filters<E> filter(E[] array) {
    return new Filters<>(requireNonNull(array, "The array to filter should not be null"));
  }

  private Filters(Iterable<E> iterable) {
    this.initialIterable = iterable;
    // copy list to avoid modifying iterable
    this.filteredIterable = newArrayList(iterable);
  }

  private Filters(E[] array) {
    this(newArrayList(array));
  }

  /**
   * Filter the underlying group, keeping only elements satisfying the given {@link Condition}.<br>
   * Same as {@link #having(Condition)} - pick the method you prefer to have the most readable code.
   *
   * <pre><code class='java'> List&lt;Player&gt; players = ...;
   *
   * Condition&lt;Player&gt; potentialMVP = new Condition&lt;Player&gt;("is a possible MVP") {
   *   public boolean matches(Player player) {
   *     return player.getPointsPerGame() &gt; 20 &amp;&amp; player.getAssistsPerGame() &gt; 7;
   *   };
   * };
   *
   * // use filter static method to build Filters
   * assertThat(filter(players).being(potentialMVP).get()).containsOnly(james, rose);</code></pre>
   *
   * @param condition the filter {@link Condition}.
   * @return this {@link Filters} to chain other filter operations.
   * @throws IllegalArgumentException if the given condition is {@code null}.
   */
  public Filters<E> being(Condition<? super E> condition) {
    return having(condition);
  }

  /**
   * Filter the underlying group, keeping only elements satisfying the given {@link Condition}.<br>
   * Same as {@link #being(Condition)} - pick the method you prefer to have the most readable code.
   *
   * <pre><code class='java'> List&lt;Player&gt; players = ...;
   *
   * Condition&lt;Player&gt; mvpStats = new Condition&lt;Player&gt;("is a possible MVP") {
   *   public boolean matches(Player player) {
   *     return player.getPointsPerGame() &gt; 20 &amp;&amp; player.getAssistsPerGame() &gt; 7;
   *   };
   * };
   *
   * // use filter static method to build Filters
   * assertThat(filter(players).having(mvpStats).get()).containsOnly(james, rose);</code></pre>
   *
   * @param condition the filter {@link Condition}.
   * @return this {@link Filters} to chain other filter operations.
   * @throws IllegalArgumentException if the given condition is {@code null}.
   */
  public Filters<E> having(Condition<? super E> condition) {
    checkArgument(condition != null, "The filter condition should not be null");
    return applyFilterCondition(condition);
  }

  private Filters<E> applyFilterCondition(Condition<? super E> condition) {
    this.filteredIterable = filteredIterable.stream().filter(condition::matches).collect(toList());
    return this;
  }

  /**
   * Filter the underlying group, keeping only elements with a property equals to given value.
   * <p>
   * Let's, for example, filter Employees with name "Alex" :
   * <pre><code class='java'> filter(employees).with("name", "Alex").get();</code></pre>
   *
   * which is shortcut of :
   * <pre><code class='java'> filter(employees).with("name").equalsTo("Alex").get();</code></pre>
   *
   * @param propertyOrFieldName the name of the property/field whose value will compared to given value. It may be a
   *          nested property.
   * @param propertyValue the expected property value.
   * @return this {@link Filters} to chain other filter operations.
   * @throws IntrospectionError if an element in the given {@code Iterable} does not have a property with a given
   *           propertyOrFieldName.
   * @throws IllegalArgumentException if the given propertyOrFieldName is {@code null}.
   */
  public Filters<E> with(String propertyOrFieldName, Object propertyValue) {
    validatePropertyOrFieldName(propertyOrFieldName);
    propertyOrFieldNameToFilterOn = propertyOrFieldName;
    return equalsTo(propertyValue);
  }

  /**
   * Sets the name of the property used for filtering, it may be a nested property like
   * <code>"address.street.name"</code>.
   * <p>
   * The typical usage is to chain this call with a comparison method, for example :
   * <pre><code class='java'> filter(employees).with("name").equalsTo("Alex").get();</code></pre>
   *
   * @param propertyOrFieldName the name of the property/field used for filtering. It may be a nested property.
   * @return this {@link Filters} to chain other filter operation.
   * @throws IllegalArgumentException if the given propertyOrFieldName is {@code null}.
   */
  public Filters<E> with(String propertyOrFieldName) {
    validatePropertyOrFieldName(propertyOrFieldName);
    propertyOrFieldNameToFilterOn = propertyOrFieldName;
    return this;
  }

  private void validatePropertyOrFieldName(String propertyOrFieldName) {
    checkArgument(!Strings.isNullOrEmpty(propertyOrFieldName),
                  "The property/field name to filter on should not be null or empty");
  }

  /**
   * Alias of {@link #with(String)} for synthetic sugar to write things like :
   * <pre><code class='java'> filter(employees).with("name").equalsTo("Alex").and("job").notEqualsTo("lawyer").get();</code></pre>
   *
   * @param propertyOrFieldName the name of the property/field used for filtering. It may be a nested property.
   * @return this {@link Filters} to chain other filter operation.
   * @throws IllegalArgumentException if the given propertyOrFieldName is {@code null}.
   */
  public Filters<E> and(String propertyOrFieldName) {
    return with(propertyOrFieldName);
  }

  /**
   * Filters the underlying iterable to keep object with property (specified by {@link #with(String)}) <b>equals to</b>
   * given value.
   * <p>
   * Typical usage :
   * <pre><code class='java'> filter(employees).with("name").equalsTo("Luke").get();</code></pre>
   *
   * @param propertyValue the filter value.
   * @return this {@link Filters} to chain other filter operation.
   * @throws IllegalArgumentException if the property name to filter on has not been set.
   */
  public Filters<E> equalsTo(Object propertyValue) {
    checkPropertyNameToFilterOnIsNotNull();
    this.filteredIterable = filteredIterable.stream().filter(element -> {
      Object propertyValueOfCurrentElement = PROPERTY_OR_FIELD_SUPPORT.getValueOf(propertyOrFieldNameToFilterOn, element);
      return deepEquals(propertyValueOfCurrentElement, propertyValue);
    }).collect(toList());
    return this;
  }

  /**
   * Filters the underlying iterable to keep object with property (specified by {@link #with(String)}) <b>not equals
   * to</b> given
   * value.
   * <p>
   * Typical usage :
   * <pre><code class='java'> filter(employees).with("name").notEqualsTo("Vader").get();</code></pre>
   *
   * @param propertyValue the filter value.
   * @return this {@link Filters} to chain other filter operation.
   * @throws IllegalArgumentException if the property name to filter on has not been set.
   */
  public Filters<E> notEqualsTo(Object propertyValue) {
    checkPropertyNameToFilterOnIsNotNull();
    this.filteredIterable = filteredIterable.stream().filter(element -> {
      Object propertyValueOfCurrentElement = PROPERTY_OR_FIELD_SUPPORT.getValueOf(propertyOrFieldNameToFilterOn, element);
      return !deepEquals(propertyValueOfCurrentElement, propertyValue);
    }).collect(toList());
    return this;
  }

  private void checkPropertyNameToFilterOnIsNotNull() {
    checkArgument(propertyOrFieldNameToFilterOn != null,
                  "The property name to filter on has not been set - no filtering is possible");
  }

  /**
   * Filters the underlying iterable to keep object with property (specified by {@link #with(String)}) <b>equals to</b>
   * one of the given values.
   * <p>
   * Typical usage :
   * <pre><code class='java'>filter(players).with("team").in("Bulls", "Lakers").get();</code></pre>
   *
   * @param propertyValues the filter values.
   * @return this {@link Filters} to chain other filter operation.
   * @throws IllegalArgumentException if the property name to filter on has not been set.
   */
  public Filters<E> in(Object... propertyValues) {
    checkPropertyNameToFilterOnIsNotNull();
    this.filteredIterable = filteredIterable.stream().filter(element -> {
      Object propertyValueOfCurrentElement = PROPERTY_OR_FIELD_SUPPORT.getValueOf(propertyOrFieldNameToFilterOn, element);
      return isItemInArray(propertyValueOfCurrentElement, propertyValues);
    }).collect(toList());
    return this;
  }

  /**
   * Filters the underlying iterable to keep object with property (specified by {@link #with(String)}) <b>not in</b> the
   * given values.
   * <p>
   * Typical usage :
   * <pre><code class='java'> filter(players).with("team").notIn("Heat", "Lakers").get();</code></pre>
   *
   * @param propertyValues the filter values.
   * @return this {@link Filters} to chain other filter operation.
   * @throws IllegalArgumentException if the property name to filter on has not been set.
   */
  public Filters<E> notIn(Object... propertyValues) {
    checkPropertyNameToFilterOnIsNotNull();
    this.filteredIterable = filteredIterable.stream().filter(element -> {
      Object propertyValueOfCurrentElement = PROPERTY_OR_FIELD_SUPPORT.getValueOf(propertyOrFieldNameToFilterOn, element);
      return !isItemInArray(propertyValueOfCurrentElement, propertyValues);
    }).collect(toList());
    return this;
  }

  /**
   * Returns <code>true</code> if given item is in given array, <code>false</code> otherwise.
   *
   * @param item the object to look for in arrayOfValues
   * @param arrayOfValues the array of values
   * @return <code>true</code> if given item is in given array, <code>false</code> otherwise.
   */
  private static boolean isItemInArray(Object item, Object[] arrayOfValues) {
    for (Object value : arrayOfValues)
      if (deepEquals(value, item)) return true;
    return false;
  }

  /**
   * Returns the resulting filtered Iterable&lt;E&gt; (even if the constructor parameter type was an array).
   *
   * @return the Iterable&lt;E&gt; containing the filtered elements.
   */
  public List<E> get() {
    return filteredIterable;
  }

}
