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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.core.api.recursive.comparison.dualvalue;

import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.core.util.Sets.newTreeSet;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.recursive.comparison.DualValue;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

class DualValue_iterableValues_Test {

  private static final List<String> PATH = list("foo", "bar");

  @ParameterizedTest
  @MethodSource("orderedCollections")
  void isActualAnOrderedCollection_should_return_true_when_actual_is_an_ordered_collection(Iterable<?> actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "");
    // WHEN
    boolean isActualAnOrderedCollection = dualValue.isActualAnOrderedCollection();
    // THEN
    then(isActualAnOrderedCollection).isTrue();
  }

  @ParameterizedTest
  @MethodSource("orderedCollections")
  void isExpectedAnOrderedCollection_should_return_true_when_expected_is_an_ordered_collection(Iterable<?> expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected);
    // WHEN
    boolean isExpectedAnOrderedCollection = dualValue.isExpectedAnOrderedCollection();
    // THEN
    then(isExpectedAnOrderedCollection).isTrue();
  }

  static Stream<Iterable<?>> orderedCollections() {
    return Stream.of(list("a", "b"), newTreeSet("a", "b"), newLinkedHashSet("a", "b"));
  }

  @ParameterizedTest
  @MethodSource("nonOrdered")
  void isActualAnOrderedCollection_should_return_false_when_actual_is_not_an_ordered_collection(Object actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "");
    // WHEN
    boolean isActualAnOrderedCollection = dualValue.isActualAnOrderedCollection();
    // THEN
    then(isActualAnOrderedCollection).isFalse();
  }

  @ParameterizedTest
  @MethodSource("nonOrdered")
  void isExpectedAnOrderedCollection_should_return_false_when_expected_is_not_an_ordered_collection(Object expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected);
    // WHEN
    boolean isExpectedAnOrderedCollection = dualValue.isExpectedAnOrderedCollection();
    // THEN
    then(isExpectedAnOrderedCollection).isFalse();
  }

  static Stream<Object> nonOrdered() {
    return Stream.of(newHashSet("a", "b"), "abc", null);
  }

  @ParameterizedTest
  @MethodSource("iterables")
  void isActualAnIterable_should_return_true_when_actual_is_an_ordered_collection(Object actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "");
    // WHEN
    boolean isActualAnIterable = dualValue.isActualAnIterable();
    // THEN
    then(isActualAnIterable).isTrue();
  }

  @ParameterizedTest
  @MethodSource("iterables")
  void isExpectedAnIterable_should_return_true_when_expected_is_an_iterable(Object expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected);
    // WHEN
    boolean isExpectedAnIterable = dualValue.isExpectedAnIterable();
    // THEN
    then(isExpectedAnIterable).isTrue();
  }

  static Stream<Iterable<?>> iterables() {
    return Stream.of(list("a", "b"), newTreeSet("a", "b"), newLinkedHashSet("a", "b"), newHashSet("a", "b"));
  }

  @ParameterizedTest
  @MethodSource("nonIterables")
  void isActualAnIterable_should_return_false_when_actual_is_not_an_iterable(Object actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "");
    // WHEN
    boolean isActualAnIterable = dualValue.isActualAnIterable();
    // THEN
    then(isActualAnIterable).isFalse();
  }

  @ParameterizedTest
  @MethodSource("nonIterables")
  void isExpectedAnIterable_should_return_false_when_expected_is_not_an_iterable(Object expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected);
    // WHEN
    boolean isExpectedAnIterable = dualValue.isExpectedAnIterable();
    // THEN
    then(isExpectedAnIterable).isFalse();
  }

  static Stream<Object> nonIterables() {
    return Stream.of(123, "abc", array("a", "b"), Path.of("/tmp"));
  }

  @ParameterizedTest
  @MethodSource("iterableJsonNodes")
  void isExpectedAnIterable_should_return_true_when_expected_is_an_array_json_node(JsonNode expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected.findValue("value"));
    // WHEN
    boolean isExpectedAnIterable = dualValue.isExpectedAnIterable();
    // THEN
    then(isExpectedAnIterable).isTrue();
  }

  @ParameterizedTest
  @MethodSource("nonIterableJsonNodes")
  void isExpectedAnIterable_should_return_false_when_expected_is_a_json_node_that_should_not_be_treated_as_an_iterable(JsonNode expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected.findValue("value"));
    // WHEN
    boolean isExpectedAnIterable = dualValue.isExpectedAnIterable();
    // THEN
    then(isExpectedAnIterable).isFalse();
  }

  @ParameterizedTest
  @MethodSource("iterableJsonNodes")
  void isActualAnIterable_should_return_true_when_actual_is_an_array_json_node(JsonNode actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual.findValue("value"), "");
    // WHEN
    boolean isActualAnIterable = dualValue.isActualAnIterable();
    // THEN
    then(isActualAnIterable).isTrue();
  }

  @ParameterizedTest
  @MethodSource("nonIterableJsonNodes")
  void isActualAnIterable_should_return_false_when_actual_is_a_json_node_that_should_not_be_treated_as_an_iterable(JsonNode actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual.findValue("value"), "");
    // WHEN
    boolean isActualAnIterable = dualValue.isActualAnIterable();
    // THEN
    then(isActualAnIterable).isFalse();
  }

  static Stream<JsonNode> iterableJsonNodes() {
    return Stream.of("{\"value\": []}")
                 .map(DualValue_iterableValues_Test::toJsonNode);
  }

  static Stream<JsonNode> nonIterableJsonNodes() {
    return Stream.of("{\"value\": \"foo\"}", "{\"value\": 42}", "{\"value\": true}", "{\"value\": {}}")
                 .map(DualValue_iterableValues_Test::toJsonNode);
  }

  private static JsonNode toJsonNode(String value) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.readTree(value);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

}
