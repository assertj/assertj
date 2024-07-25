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

import static java.util.Collections.unmodifiableCollection;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.Condition;
import org.assertj.core.description.Description;
import org.assertj.core.description.JoinDescription;
import org.assertj.core.util.Streams;
import org.assertj.core.util.VisibleForTesting;

/**
 * Join of two or more <code>{@link Condition}</code>s.
 *
 * @author Yvonne Wang
 * @author Mikhail Mazursky
 *
 * @param <T> the type of object this condition accepts.
 */
public abstract class Join<T> extends Condition<T> {

  protected static final String SUFFIX_DELIMITER = "]";
  protected static final String PREFIX_DELIMITER = ":[";

  @VisibleForTesting
  Collection<Condition<? super T>> conditions;

  /**
   * Creates a new <code>{@link Join}</code>.
   * @param conditions the conditions to join.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws NullPointerException if any of the elements in the given array is {@code null}.
   */
  @SafeVarargs
  protected Join(Condition<? super T>... conditions) {
    this(Arrays.stream(checkNotNullConditions(conditions)));
  }

  /**
   * Creates a new <code>{@link Join}</code>.
   * @param conditions the conditions to join.
   * @throws NullPointerException if the given iterable is {@code null}.
   * @throws NullPointerException if any of the elements in the given iterable is {@code null}.
   */
  protected Join(Iterable<? extends Condition<? super T>> conditions) {
    this(Streams.stream(checkNotNullConditions(conditions)));
  }

  private Join(Stream<? extends Condition<? super T>> stream) {
    conditions = stream.map(Join::notNull).collect(toList());
    calculateDescription();
  }

  private static <T> T checkNotNullConditions(T conditions) {
    return requireNonNull(conditions, "The given conditions should not be null");
  }

  /**
   * method used to prefix the subclass join description, ex: "all of"
   * @return the prefix to use to build the description.
   */
  public abstract String descriptionPrefix();

  /**
   * method used to calculate the subclass join description
   */
  private void calculateDescription() {
    List<Description> conditionsDescriptions = conditions.stream()
                                                         .map(Condition::description)
                                                         .collect(toList());
    String prefix = descriptionPrefix() + PREFIX_DELIMITER;
    describedAs(new JoinDescription(prefix, SUFFIX_DELIMITER, conditionsDescriptions));
  }

  @Override
  public Description description() {
    calculateDescription();
    return super.description();
  }

  @Override
  public Description conditionDescriptionWithStatus(T actual) {
    List<Description> descriptionsWithStatus = conditions.stream()
                                                         .map(condition -> condition.conditionDescriptionWithStatus(actual))
                                                         .collect(toList());
    String prefix = status(actual).label + " " + descriptionPrefix() + PREFIX_DELIMITER;
    return new JoinDescription(prefix, SUFFIX_DELIMITER, descriptionsWithStatus);
  }

  private static <T> T notNull(T condition) {
    return requireNonNull(condition, "The given conditions should not have null entries");
  }

  /**
   * Returns the conditions to join.
   * @return the conditions to join.
   */
  public final Collection<Condition<? super T>> conditions() {
    return unmodifiableCollection(conditions);
  }
}
