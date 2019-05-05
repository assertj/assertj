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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.core.util.Sets.newTreeSet;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class DualValue_iterableValues_Test {

  private static final List<String> PATH = list("foo", "bar");

  @ParameterizedTest
  @MethodSource("orderedCollections")
  public void isActualFieldAnOrderedCollection_should_return_true_when_actual_is_an_ordered_collection(Iterable<?> actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "");
    // WHEN
    boolean isActualFieldAnOrderedCollection = dualValue.isActualFieldAnOrderedCollection();
    // THEN
    assertThat(isActualFieldAnOrderedCollection).isTrue();
  }

  @ParameterizedTest
  @MethodSource("orderedCollections")
  public void isExpectedFieldAnOrderedCollection_should_return_true_when_expected_is_an_ordered_collection(Iterable<?> expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected);
    // WHEN
    boolean isExpectedFieldAnOrderedCollection = dualValue.isExpectedFieldAnOrderedCollection();
    // THEN
    assertThat(isExpectedFieldAnOrderedCollection).isTrue();
  }

  static Stream<Iterable<?>> orderedCollections() {
    return Stream.of(list("a", "b"), newTreeSet("a", "b"), newLinkedHashSet("a", "b"));
  }

  @ParameterizedTest
  @MethodSource("nonOrdered")
  public void isActualFieldAnOrderedCollection_should_return_false_when_actual_is_not_an_ordered_collection(Object actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "");
    // WHEN
    boolean isActualFieldAnOrderedCollection = dualValue.isActualFieldAnOrderedCollection();
    // THEN
    assertThat(isActualFieldAnOrderedCollection).isFalse();
  }

  @ParameterizedTest
  @MethodSource("nonOrdered")
  public void isExpectedFieldAnOrderedCollection_should_return_false_when_expected_is_not_an_ordered_collection(Object expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected);
    // WHEN
    boolean isExpectedFieldAnOrderedCollection = dualValue.isExpectedFieldAnOrderedCollection();
    // THEN
    assertThat(isExpectedFieldAnOrderedCollection).isFalse();
  }

  static Stream<Object> nonOrdered() {
    return Stream.of(newHashSet("a", "b"), "abc", null);
  }

  @ParameterizedTest
  @MethodSource("iterables")
  public void isActualFieldAnIterable_should_return_true_when_actual_is_an_ordered_collection(Object actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "");
    // WHEN
    boolean isActualFieldAnIterable = dualValue.isActualFieldAnIterable();
    // THEN
    assertThat(isActualFieldAnIterable).isTrue();
  }

  @ParameterizedTest
  @MethodSource("iterables")
  public void isExpectedFieldAnIterable_should_return_true_when_expected_is_an_iterable(Object expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected);
    // WHEN
    boolean isExpectedFieldAnIterable = dualValue.isExpectedFieldAnIterable();
    // THEN
    assertThat(isExpectedFieldAnIterable).isTrue();
  }

  static Stream<Iterable<?>> iterables() {
    return Stream.of(list("a", "b"), newTreeSet("a", "b"), newLinkedHashSet("a", "b"), newHashSet("a", "b"));
  }

  @ParameterizedTest
  @MethodSource("nonIterables")
  public void isActualFieldAnIterable_should_return_false_when_actual_is_not_an_iterable(Object actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "");
    // WHEN
    boolean isActualFieldAnIterable = dualValue.isActualFieldAnIterable();
    // THEN
    assertThat(isActualFieldAnIterable).isFalse();
  }

  @ParameterizedTest
  @MethodSource("nonIterables")
  public void isExpectedFieldAnIterable_should_return_false_when_expected_is_not_an_iterable(Object expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected);
    // WHEN
    boolean isExpectedFieldAnIterable = dualValue.isExpectedFieldAnIterable();
    // THEN
    assertThat(isExpectedFieldAnIterable).isFalse();
  }

  static Stream<Object> nonIterables() {
    return Stream.of(123, "abc", array("a", "b"));
  }

}
