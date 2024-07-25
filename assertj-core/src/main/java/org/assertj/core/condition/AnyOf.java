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
package org.assertj.core.condition;

import org.assertj.core.api.Condition;

/**
 * Returns {@code true} if any of the joined conditions is satisfied.
 *
 * @author Yvonne Wang
 * @author Mikhail Mazursky
 *
 * @param <T> the type of object this condition accepts.
 */
public class AnyOf<T> extends Join<T> {

  /**
   * Creates a new <code>{@link AnyOf}</code>
   *
   * @param <T> the type of object the given condition accept.
   * @param conditions the conditions to evaluate.
   * @return the created {@code AnyOf}.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws NullPointerException if any of the elements in the given array is {@code null}.
   */
  @SafeVarargs
  public static <T> Condition<T> anyOf(Condition<? super T>... conditions) {
    return new AnyOf<>(conditions);
  }

  /**
   * Creates a new <code>{@link AnyOf}</code>
   * @param <T> the type of object the given condition accept.
   * @param conditions the conditions to evaluate.
   * @return the created {@code AnyOf}.
   * @throws NullPointerException if the given iterable is {@code null}.
   * @throws NullPointerException if any of the elements in the given iterable is {@code null}.
   */
  public static <T> Condition<T> anyOf(Iterable<? extends Condition<? super T>> conditions) {
    return new AnyOf<>(conditions);
  }

  @SafeVarargs
  private AnyOf(Condition<? super T>... conditions) {
    super(conditions);
  }

  private AnyOf(Iterable<? extends Condition<? super T>> conditions) {
    super(conditions);
  }

  /** {@inheritDoc} */
  @Override
  public boolean matches(T value) {
    return conditions.stream().anyMatch(condition -> condition.matches(value));
  }

  @Override
  public String descriptionPrefix() {
    return "any of";
  }
}
