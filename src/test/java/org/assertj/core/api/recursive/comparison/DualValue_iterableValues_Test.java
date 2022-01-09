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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.core.util.Sets.newTreeSet;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BinaryNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.node.TextNode;

class DualValue_iterableValues_Test {

  private static final List<String> PATH = list("foo", "bar");

  @ParameterizedTest
  @MethodSource("orderedCollections")
  void isActualFieldAnOrderedCollection_should_return_true_when_actual_is_an_ordered_collection(Iterable<?> actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "");
    // WHEN
    boolean isActualFieldAnOrderedCollection = dualValue.isActualFieldAnOrderedCollection();
    // THEN
    assertThat(isActualFieldAnOrderedCollection).isTrue();
  }

  @ParameterizedTest
  @MethodSource("orderedCollections")
  void isExpectedFieldAnOrderedCollection_should_return_true_when_expected_is_an_ordered_collection(Iterable<?> expected) {
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
  void isActualFieldAnOrderedCollection_should_return_false_when_actual_is_not_an_ordered_collection(Object actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "");
    // WHEN
    boolean isActualFieldAnOrderedCollection = dualValue.isActualFieldAnOrderedCollection();
    // THEN
    assertThat(isActualFieldAnOrderedCollection).isFalse();
  }

  @ParameterizedTest
  @MethodSource("nonOrdered")
  void isExpectedFieldAnOrderedCollection_should_return_false_when_expected_is_not_an_ordered_collection(Object expected) {
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
  void isActualFieldAnIterable_should_return_true_when_actual_is_an_ordered_collection(Object actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "");
    // WHEN
    boolean isActualFieldAnIterable = dualValue.isActualFieldAnIterable();
    // THEN
    assertThat(isActualFieldAnIterable).isTrue();
  }

  @ParameterizedTest
  @MethodSource("iterables")
  void isExpectedFieldAnIterable_should_return_true_when_expected_is_an_iterable(Object expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected);
    // WHEN
    boolean isExpectedFieldAnIterable = dualValue.isExpectedFieldAnIterable();
    // THEN
    assertThat(isExpectedFieldAnIterable).isTrue();
  }

  static Stream<Iterable<?>> iterables() {
    return Stream.of(list("a", "b"), newTreeSet("a", "b"), newLinkedHashSet("a", "b"), newHashSet("a", "b"), new ObjectNode(null),
                     new ArrayNode(null));
  }

  @ParameterizedTest
  @MethodSource("nonIterables")
  void isActualFieldAnIterable_should_return_false_when_actual_is_not_an_iterable(Object actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "");
    // WHEN
    boolean isActualFieldAnIterable = dualValue.isActualFieldAnIterable();
    // THEN
    assertThat(isActualFieldAnIterable).isFalse();
  }

  @ParameterizedTest
  @MethodSource("nonIterables")
  void isExpectedFieldAnIterable_should_return_false_when_expected_is_not_an_iterable(Object expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected);
    // WHEN
    boolean isExpectedFieldAnIterable = dualValue.isExpectedFieldAnIterable();
    // THEN
    assertThat(isExpectedFieldAnIterable).isFalse();
  }

  static Stream<Object> nonIterables() {
    // even though Path and ValueNode are iterables, they must not be considered as such
    return Stream.of(123, "abc", array("a", "b"), Paths.get("/tmp"), new TextNode("foo"), new IntNode(42), BooleanNode.TRUE,
                     new POJONode(new Object()), MissingNode.getInstance(), NullNode.getInstance(), new BinaryNode(new byte[0]));
  }

}
