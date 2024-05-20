/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.recursive.comparison;

import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.core.util.Sets.newTreeSet;

import java.nio.file.Paths;
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
  void isActualFieldAnOrderedCollection_should_return_true_when_actual_is_an_ordered_collection(Iterable<?> actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "");
    // WHEN
    boolean isActualFieldAnOrderedCollection = dualValue.isActualFieldAnOrderedCollection();
    // THEN
    then(isActualFieldAnOrderedCollection).isTrue();
  }

  @ParameterizedTest
  @MethodSource("orderedCollections")
  void isExpectedFieldAnOrderedCollection_should_return_true_when_expected_is_an_ordered_collection(Iterable<?> expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected);
    // WHEN
    boolean isExpectedFieldAnOrderedCollection = dualValue.isExpectedFieldAnOrderedCollection();
    // THEN
    then(isExpectedFieldAnOrderedCollection).isTrue();
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
    then(isActualFieldAnOrderedCollection).isFalse();
  }

  @ParameterizedTest
  @MethodSource("nonOrdered")
  void isExpectedFieldAnOrderedCollection_should_return_false_when_expected_is_not_an_ordered_collection(Object expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected);
    // WHEN
    boolean isExpectedFieldAnOrderedCollection = dualValue.isExpectedFieldAnOrderedCollection();
    // THEN
    then(isExpectedFieldAnOrderedCollection).isFalse();
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
    then(isActualFieldAnIterable).isTrue();
  }

  @ParameterizedTest
  @MethodSource("iterables")
  void isExpectedFieldAnIterable_should_return_true_when_expected_is_an_iterable(Object expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected);
    // WHEN
    boolean isExpectedFieldAnIterable = dualValue.isExpectedFieldAnIterable();
    // THEN
    then(isExpectedFieldAnIterable).isTrue();
  }

  static Stream<Iterable<?>> iterables() {
    return Stream.of(list("a", "b"), newTreeSet("a", "b"), newLinkedHashSet("a", "b"), newHashSet("a", "b"));
  }

  @ParameterizedTest
  @MethodSource("nonIterables")
  void isActualFieldAnIterable_should_return_false_when_actual_is_not_an_iterable(Object actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "");
    // WHEN
    boolean isActualFieldAnIterable = dualValue.isActualFieldAnIterable();
    // THEN
    then(isActualFieldAnIterable).isFalse();
  }

  @ParameterizedTest
  @MethodSource("nonIterables")
  void isExpectedFieldAnIterable_should_return_false_when_expected_is_not_an_iterable(Object expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected);
    // WHEN
    boolean isExpectedFieldAnIterable = dualValue.isExpectedFieldAnIterable();
    // THEN
    then(isExpectedFieldAnIterable).isFalse();
  }

  static Stream<Object> nonIterables() {
    return Stream.of(123, "abc", array("a", "b"), Paths.get("/tmp"));
  }

  @ParameterizedTest
  @MethodSource("iterableJsonNodes")
  void isExpectedFieldAnIterable_should_return_true_when_expected_is_an_array_json_node(JsonNode expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected.findValue("value"));
    // WHEN
    boolean isExpectedFieldAnIterable = dualValue.isExpectedFieldAnIterable();
    // THEN
    then(isExpectedFieldAnIterable).isTrue();
  }

  @ParameterizedTest
  @MethodSource("nonIterableJsonNodes")
  void isExpectedFieldAnIterable_should_return_false_when_expected_is_a_json_node_that_should_not_be_treated_as_an_iterable(JsonNode expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected.findValue("value"));
    // WHEN
    boolean isExpectedFieldAnIterable = dualValue.isExpectedFieldAnIterable();
    // THEN
    then(isExpectedFieldAnIterable).isFalse();
  }

  @ParameterizedTest
  @MethodSource("iterableJsonNodes")
  void isActualFieldAnIterable_should_return_true_when_actual_is_an_array_json_node(JsonNode actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual.findValue("value"), "");
    // WHEN
    boolean isActualFieldAnIterable = dualValue.isActualFieldAnIterable();
    // THEN
    then(isActualFieldAnIterable).isTrue();
  }

  @ParameterizedTest
  @MethodSource("nonIterableJsonNodes")
  void isActualFieldAnIterable_should_return_false_when_actual_is_a_json_node_that_should_not_be_treated_as_an_iterable(JsonNode actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual.findValue("value"), "");
    // WHEN
    boolean isActualFieldAnIterable = dualValue.isActualFieldAnIterable();
    // THEN
    then(isActualFieldAnIterable).isFalse();
  }

  static Stream<JsonNode> iterableJsonNodes() {
    return Stream.of("{\"value\": []}")
                 .map(json -> toJsonNode(json));
  }

  static Stream<JsonNode> nonIterableJsonNodes() {
    return Stream.of("{\"value\": \"foo\"}", "{\"value\": 42}", "{\"value\": true}", "{\"value\": {}}")
                 .map(json -> toJsonNode(json));
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
