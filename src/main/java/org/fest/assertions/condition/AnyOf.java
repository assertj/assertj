/*
 * Created on Feb 5, 2011
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
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.condition;

import java.util.*;

import org.fest.assertions.core.Condition;
import org.fest.util.VisibleForTesting;

/**
 * Returns {@code true} if any of the given conditions is satisfied.
 * @param <T> the type of object this condition accepts.
 *
 * @author Yvonne Wang
 */
public class AnyOf<T> extends Condition<T> {

  @VisibleForTesting final Collection<Condition<T>> conditions;

  /**
   * Creates a new <code>{@link AnyOf}</code>
   * @param <T> the type of object the given condition accept.
   * @param conditions the conditions to evaluate.
   * @return the created {@code AnyOf}.
   * @throws NullPointerException if the given array is {@code null}.
   */
  public static <T> Condition<T> anyOf(Condition<T>...conditions) {
    if (conditions == null) throw conditionsIsNull();
    return new AnyOf<T>(listWithoutNulls(conditions));
  }

  private static <T> List<Condition<T>> listWithoutNulls(Condition<T>... conditions) {
    List<Condition<T>> list = new ArrayList<Condition<T>>();
    for (Condition<T> condition : conditions) list.add(notNull(condition));
    return list;
  }

  /**
   * Creates a new <code>{@link AnyOf}</code>
   * @param <T> the type of object the given condition accept.
   * @param conditions the conditions to evaluate.
   * @return the created {@code AnyOf}.
   * @throws NullPointerException if the given array is {@code null}.
   */
  public static <T> Condition<T> anyOf(Collection<Condition<T>> conditions) {
    if (conditions == null) throw conditionsIsNull();
    return new AnyOf<T>(listWithoutNulls(conditions));
  }

  private static NullPointerException conditionsIsNull() {
    return new NullPointerException("The given conditions should not be null");
  }

  private static <T> List<Condition<T>> listWithoutNulls(Collection<Condition<T>> conditions) {
    List<Condition<T>> list = new ArrayList<Condition<T>>();
    for (Condition<T> condition : conditions) list.add(notNull(condition));
    return list;
  }

  private static <T> Condition<T> notNull(Condition<T> condition) {
    if (condition == null) throw new NullPointerException("The given conditions should not have null values");
    return condition;
  }

  private AnyOf(Collection<Condition<T>> conditions) {
    this.conditions = new ArrayList<Condition<T>>(conditions);
  }

  /** {@inheritDoc} */
  @Override public boolean matches(T value) {
    for (Condition<T> condition : conditions)
      if (condition.matches(value)) return true;
    return false;
  }
}
