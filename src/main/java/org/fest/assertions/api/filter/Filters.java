/*
 * Created on Feb 22, 2011
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
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.api.filter;

import static org.fest.util.Lists.newArrayList;
import static org.fest.util.Objects.areEqual;

import java.util.ArrayList;
import java.util.List;

import org.fest.assertions.core.Condition;
import org.fest.assertions.internal.PropertySupport;
import org.fest.util.IntrospectionError;
import org.fest.util.VisibleForTesting;

/**
 * Filters the elements of a given <code>{@link Iterable}</code> or array according to the specified filter criteria.
 * <p>
 * Filter criteria can be expressed either by a {@link Condition} or a pseudo filter language on elements properties.
 * <p>
 * Note that the given {@link Iterable} or array is not modified, the filters are performed on a copy.
 * <p>
 * With {@link Condition} :
 * 
 * <pre>
 * List&lt;Player&gt; players = ...; 
 *   
 * Condition&lt;Player&gt; potentialMVP = new Condition&lt;Player&gt;("is a possible MVP"){
 *   public boolean matches(Player player) {
 *     return player.getPointsPerGame() > 20 && player.getAssistsPerGame() > 7;
 *   };
 * };
 * 
 * // use filter static method to build Filters
 * assertThat(filter(players).being(potentialMVP).get()).containsOnly(james, rose)
 * </pre>
 * 
 * With pseudo filter language on element properties :
 * 
 * <pre>
 * assertThat(filter(players).with("pointsPerGame").greaterThan(20)
 *                           .and("assistsPerGame").greaterThan(7)
 *                           .get()).containsOnly(james, rose);</pre>
 * 
 * @param <E> the type of element of group to filter.
 * 
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class Filters<E> {

  // initialIterable is never modified, it represents the group before any filters have been performed
  @VisibleForTesting
  final Iterable<E> initialIterable;
  Iterable<E> filteredIterable;

  private PropertySupport propertySupport = PropertySupport.instance();

  /**
   * The name of the property used for filtering.
   */
  private String propertyNameToFilterOn;

  /**
   * Creates a new <code>{@link Filters}</code> with the {@link Iterable} to filter.
   * <p>
   * Chain this call to express filter criteria either by a {@link Condition} or a pseudo filter language on elements properties.
   * <p>
   * Note that the given {@link Iterable} is not modified, the filters are performed on a copy.
   * <p>
   * - With {@link Condition} :
   * 
   * <pre>
   * List&lt;Player&gt; players = ...; 
   *   
   * Condition&lt;Player&gt; potentialMVP = new Condition&lt;Player&gt;("is a possible MVP"){
   *   public boolean matches(Player player) {
   *     return player.getPointsPerGame() > 20 && player.getAssistsPerGame() > 7;
   *   };
   * };
   * 
   * // use filter static method to build Filters
   * assertThat(filter(players).being(potentialMVP).get()).containsOnly(james, rose)
   * </pre>
   * 
   * - With pseudo filter language on element properties :
   * 
   * <pre>
   * assertThat(filter(players).with("pointsPerGame").greaterThan(20)
   *                           .and("assistsPerGame").greaterThan(7).get())
   *                           .containsOnly(james, rose);</pre>
   * 
   * @param iterable the {@code Iterable} to filter.
   * @throws NullPointerException if the given iterable is {@code null}.
   * @return the created <code>{@link Filters}</code>.
   */
  public static <E> Filters<E> filter(Iterable<E> iterable) {
    if (iterable == null) throw new NullPointerException("The iterable to filter should not be null");
    return new Filters<E>(iterable);
  }

  /**
   * Creates a new <code>{@link Filters}</code> with the array to filter.
   * <p>
   * Chain this call to express filter criteria either by a {@link Condition} or a pseudo filter language on elements properties.
   * <p>
   * Note that the given array is not modified, the filters are performed on an {@link Iterable} copy of the array.
   * <p>
   * With {@link Condition} :
   * 
   * <pre>
   * List&lt;Player&gt; players = ...; 
   *   
   * Condition&lt;Player&gt; potentialMVP = new Condition&lt;Player&gt;("is a possible MVP"){
   *   public boolean matches(Player player) {
   *     return player.getPointsPerGame() > 20 && player.getAssistsPerGame() > 7;
   *   };
   * };
   * 
   * // use filter static method to build Filters
   * assertThat(filter(players).being(potentialMVP).get()).containsOnly(james, rose);
   * </pre>
   * 
   * With pseudo filter language on element properties :
   * 
   * <pre>
   * assertThat(filter(players).with("pointsPerGame").greaterThan(20)
   *                           .and("assistsPerGame").greaterThan(7)
   *                           .get()).containsOnly(james, rose);</pre>
   * @param array the array to filter.
   * @throws NullPointerException if the given array is {@code null}.
   * @return the created <code>{@link Filters}</code>.
   */
  public static <E> Filters<E> filter(E[] array) {
    if (array == null) throw new NullPointerException("The array to filter should not be null");
    return new Filters<E>(array);
  }

  @VisibleForTesting
  Filters(Iterable<E> iterable) {
    this.initialIterable = iterable;
    // copy list to avoid modifying iterable
    this.filteredIterable = newArrayList(iterable);
  }

  @VisibleForTesting
  Filters(E[] array) {
    List<E> iterable = new ArrayList<E>(array.length);
    for (int i = 0; i < array.length; i++) {
      iterable.add(array[i]);
    }
    this.initialIterable = iterable;
    // copy list to avoid modifying iterable
    this.filteredIterable = newArrayList(iterable);
  }

  /**
   * Filter the underlying group, keeping only elements satisfying the given {@link Condition}.<br>
   * Same as {@link #having(Condition)} - pick the method you prefer to have the most readable code.
   * 
   * <pre>
   * List&lt;Player&gt; players = ...; 
   *   
   * Condition&lt;Player&gt; potentialMVP = new Condition&lt;Player&gt;("is a possible MVP") {
   *   public boolean matches(Player player) {
   *     return player.getPointsPerGame() > 20 && player.getAssistsPerGame() > 7;
   *   };
   * };
   * 
   * // use filter static method to build Filters
   * assertThat(filter(players).being(potentialMVP).get()).containsOnly(james, rose);</pre>
   * 
   * @param condition the filter {@link Condition}.
   * @return this {@link Filters} to chain other filter operations.
   * @throws NullPointerException if the given condition is {@code null}.
   */
  public Filters<E> being(Condition<? super E> condition) {
    if (condition == null) throw new NullPointerException("The filter condition should not be null");
    return applyFilterCondition(condition);
  }

  /**
   * Filter the underlying group, keeping only elements satisfying the given {@link Condition}.<br>
   * Same as {@link #being(Condition)} - pick the method you prefer to have the most readable code.
   * 
   * <pre>
   * List&lt;Player&gt; players = ...; 
   *   
   * Condition&lt;Player&gt; mvpStats = new Condition&lt;Player&gt;("is a possible MVP") {
   *   public boolean matches(Player player) {
   *     return player.getPointsPerGame() > 20 && player.getAssistsPerGame() > 7;
   *   };
   * };
   * 
   * // use filter static method to build Filters
   * assertThat(filter(players).having(mvpStats).get()).containsOnly(james, rose);</pre>
   * 
   * @param condition the filter {@link Condition}.
   * @return this {@link Filters} to chain other filter operations.
   * @throws NullPointerException if the given condition is {@code null}.
   */
  public Filters<E> having(Condition<? super E> condition) {
    if (condition == null) throw new NullPointerException("The filter condition should not be null");
    return applyFilterCondition(condition);
  }

  private Filters<E> applyFilterCondition(Condition<? super E> condition) {
    List<E> newFilteredIterable = new ArrayList<E>();
    for (E element : filteredIterable) {
      if (condition.matches(element)) {
        newFilteredIterable.add(element);
      }
    }
    this.filteredIterable = newFilteredIterable;
    return this;
  }

  /**
   * Filter the underlying group, keeping only elements with a property equals to given value.
   * <p>
   * Let's, for example, filter Employees with name "Alex" :
   * 
   * <pre>
   * filter(employees).with("name", "Alex").get();
   * </pre>
   * which is shortcut of :
   * 
   * <pre>
   * filter(employees).with("name").equalsTo("Alex").get();
   * </pre>
   * 
   * @param propertyName the name of the property whose value will compared to given value. It may be a nested property.
   * @param propertyValue the expected property value.
   * @return this {@link Filters} to chain other filter operations.
   * @throws IntrospectionError if an element in the given {@code Iterable} does not have a property with a given propertyName.
   * @throws NullPointerException if the given propertyName is {@code null}.
   */
  public Filters<E> with(String propertyName, Object propertyValue) {
    if (propertyName == null) throw new NullPointerException("The property name to filter on should not be null");
    propertyNameToFilterOn = propertyName;
    return equalsTo(propertyValue);
  }

  /**
   * Sets the name of the property used for filtering, it may be a nested property like <code>"adress.street.name"</code>.
   * <p>
   * The typical usage is to chain this call with a comparison method, for example :
   * 
   * <pre>
   * filter(employees).with("name").equalsTo("Alex").get();
   * </pre>
   * 
   * @param propertyName the name of the property used for filtering. It may be a nested property.
   * @return this {@link Filters} to chain other filter operation.
   * @throws NullPointerException if the given propertyName is {@code null}.
   */
  public Filters<E> with(String propertyName) {
    if (propertyName == null) throw new NullPointerException("The property name to filter on should not be null");
    propertyNameToFilterOn = propertyName;
    return this;
  }

  /**
   * Alias of {@link #with(String)} for synthetic sugar to write things like :.
   * 
   * <pre>
   * filter(employees).with("name").equalsTo("Alex").and("job").notEqualsTo("lawyer").get();
   * </pre>
   * 
   * @param propertyName the name of the property used for filtering. It may be a nested property.
   * @return this {@link Filters} to chain other filter operation.
   * @throws NullPointerException if the given propertyName is {@code null}.
   */
  public Filters<E> and(String propertyName) {
    return with(propertyName);
  }

  /**
   * Filters the underlying iterable to keep object with property (specified by {@link #with(String)}) <b>equals to</b> given
   * value.
   * <p>
   * Typical usage :
   * 
   * <pre>
   * filter(employees).with("name").equalsTo("Luke").get();
   * </pre>
   * 
   * @param propertyValue the filter value.
   * @return this {@link Filters} to chain other filter operation.
   * @throws NullPointerException if the property name to filter on has not been set.
   */
  public Filters<E> equalsTo(Object propertyValue) {
    checkPropertyNameToFilterOnIsNotNull();
    List<E> newFilteredIterable = new ArrayList<E>();
    for (E element : filteredIterable) {
      // As we don't know the propertyValue class, we use Object.class
      Class<? extends Object> propertyValueClass = propertyValue == null ? Object.class : propertyValue.getClass();
      Object propertyValueOfCurrentElement = propertySupport.propertyValueOf(propertyNameToFilterOn, propertyValueClass, element);
      if (areEqual(propertyValueOfCurrentElement, propertyValue)) {
        newFilteredIterable.add(element);
      }
    }
    this.filteredIterable = newFilteredIterable;
    return this;
  }

  /**
   * Filters the underlying iterable to keep object with property (specified by {@link #with(String)}) <b>not equals to</b> given
   * value.
   * <p>
   * Typical usage :
   * 
   * <pre>
   * filter(employees).with("name").notEqualsTo("Vader").get();
   * </pre>
   * 
   * @param propertyValue the filter value.
   * @return this {@link Filters} to chain other filter operation.
   * @throws NullPointerException if the property name to filter on has not been set.
   */
  public Filters<E> notEqualsTo(Object propertyValue) {
    checkPropertyNameToFilterOnIsNotNull();
    List<E> newFilteredIterable = new ArrayList<E>();
    for (E element : filteredIterable) {
      Object propertyValueOfCurrentElement = propertySupport.propertyValueOf(propertyNameToFilterOn, propertyValue.getClass(),
          element);
      if (!areEqual(propertyValueOfCurrentElement, propertyValue)) {
        newFilteredIterable.add(element);
      }
    }
    this.filteredIterable = newFilteredIterable;
    return this;
  }

  private void checkPropertyNameToFilterOnIsNotNull() {
    if (propertyNameToFilterOn == null)
      throw new NullPointerException("The property name to filter on has not been set - no filtering is possible");
  }

  /**
   * Filters the underlying iterable to keep object with property (specified by {@link #with(String)}) <b>equals to</b> one of the
   * given values.
   * <p>
   * Typical usage :
   * 
   * <pre>
   * filter(players).with("team").in("Bulls", "Lakers").get();
   * </pre>
   * 
   * @param propertyValues the filter values.
   * @return this {@link Filters} to chain other filter operation.
   * @throws NullPointerException if the property name to filter on has not been set.
   */
  public Filters<E> in(Object... propertyValues) {
    checkPropertyNameToFilterOnIsNotNull();
    List<E> newFilteredIterable = new ArrayList<E>();
    for (E element : filteredIterable) {
      Object propertyValueOfCurrentElement = propertySupport.propertyValueOf(propertyNameToFilterOn, propertyValues.getClass()
          .getComponentType(), element);
      if (isItemInArray(propertyValueOfCurrentElement, propertyValues)) {
        newFilteredIterable.add(element);
      }
    }
    this.filteredIterable = newFilteredIterable;
    return this;
  }

  /**
   * Filters the underlying iterable to keep object with property (specified by {@link #with(String)}) <b>not in</b> the given
   * values.
   * <p>
   * Typical usage :
   * 
   * <pre>
   * filter(players).with("team").notIn("Heat", "Lakers").get();
   * </pre>
   * 
   * @param propertyValues the filter values.
   * @return this {@link Filters} to chain other filter operation.
   * @throws NullPointerException if the property name to filter on has not been set.
   */
  public Filters<E> notIn(Object... propertyValues) {
    checkPropertyNameToFilterOnIsNotNull();
    List<E> newFilteredIterable = new ArrayList<E>();
    for (E element : filteredIterable) {
      Object propertyValueOfCurrentElement = propertySupport.propertyValueOf(propertyNameToFilterOn, propertyValues.getClass()
          .getComponentType(), element);
      if (!isItemInArray(propertyValueOfCurrentElement, propertyValues)) {
        newFilteredIterable.add(element);
      }
    }
    this.filteredIterable = newFilteredIterable;
    return this;
  }

  /**
   * Returns <code>true</code> if given item is in given array, <code>false</code> otherwise.
   * @param item the object to look for in arrayOfValues
   * @param arrayOfValues the array of values
   * @return <code>true</code> if given item is in given array, <code>false</code> otherwise.
   */
  private static boolean isItemInArray(Object item, Object[] arrayOfValues) {
    for (Object value : arrayOfValues)
      if (areEqual(value, item)) return true;
    return false;
  }

  /**
   * Returns the resulting filtered Iterable&lt;E&gt; (even if the constructor parameter type was an array).
   * @return the Iterable&lt;E&gt; containing the filtered elements.
   */
  public Iterable<E> get() {
    return filteredIterable;
  }

}
