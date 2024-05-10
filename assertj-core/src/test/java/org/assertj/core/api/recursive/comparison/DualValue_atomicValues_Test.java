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
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class DualValue_atomicValues_Test {

  private static final List<String> PATH = list("foo", "bar");

  // AtomicReference

  @Test
  void isActualFieldAnAtomicReference_should_return_true_when_actual_is_an_AtomicReference() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, new AtomicReference<>("a"), "");
    // WHEN
    boolean isActualFieldAnAtomicReference = dualValue.isActualFieldAnAtomicReference();
    // THEN
    then(isActualFieldAnAtomicReference).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "abc" })
  void isActualFieldAnAtomicReference_should_return_false_when_actual_is_not_an_AtomicReference(String actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "");
    // WHEN
    boolean isActualFieldAnAtomicReference = dualValue.isActualFieldAnAtomicReference();
    // THEN
    then(isActualFieldAnAtomicReference).isFalse();
  }

  @Test
  void isExpectedFieldAnAtomicReference_should_return_true_when_expected_is_an_AtomicReference() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", new AtomicReference<>("a"));
    // WHEN
    boolean isExpectedFieldAnAtomicReference = dualValue.isExpectedFieldAnAtomicReference();
    // THEN
    then(isExpectedFieldAnAtomicReference).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "abc" })
  void isExpectedFieldAnAtomicReference_should_return_false_when_expected_is_not_an_AtomicReference(String expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected);
    // WHEN
    boolean isExpectedFieldAnAtomicReference = dualValue.isExpectedFieldAnAtomicReference();
    // THEN
    then(isExpectedFieldAnAtomicReference).isFalse();
  }

  // AtomicReferenceArray

  @Test
  void isActualFieldAnAtomicReferenceArray_should_return_true_when_actual_is_an_AtomicReferenceArray() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, new AtomicReferenceArray<>(array("a")), "");
    // WHEN
    boolean isActualFieldAnAtomicReferenceArray = dualValue.isActualFieldAnAtomicReferenceArray();
    // THEN
    then(isActualFieldAnAtomicReferenceArray).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "abc" })
  void isActualFieldAnAtomicReferenceArray_should_return_false_when_actual_is_not_an_AtomicReferenceArray(String actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "");
    // WHEN
    boolean isActualFieldAnAtomicReferenceArray = dualValue.isActualFieldAnAtomicReferenceArray();
    // THEN
    then(isActualFieldAnAtomicReferenceArray).isFalse();
  }

  @Test
  void isExpectedFieldAnAtomicReferenceArray_should_return_true_when_expected_is_an_AtomicReferenceArray() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", new AtomicReferenceArray<>(array("a")));
    // WHEN
    boolean isExpectedFieldAnAtomicReferenceArray = dualValue.isExpectedFieldAnAtomicReferenceArray();
    // THEN
    then(isExpectedFieldAnAtomicReferenceArray).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "abc" })
  void isExpectedFieldAnAtomicReferenceArray_should_return_false_when_expected_is_not_an_AtomicReferenceArray(String expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected);
    // WHEN
    boolean isExpectedFieldAnAtomicReferenceArray = dualValue.isExpectedFieldAnAtomicReferenceArray();
    // THEN
    then(isExpectedFieldAnAtomicReferenceArray).isFalse();
  }

  // AtomicInteger

  @Test
  void isActualFieldAnAtomicInteger_should_return_true_when_actual_is_an_AtomicInteger() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, new AtomicInteger(123), "");
    // WHEN
    boolean isActualFieldAnAtomicInteger = dualValue.isActualFieldAnAtomicInteger();
    // THEN
    then(isActualFieldAnAtomicInteger).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "abc" })
  void isActualFieldAnAtomicInteger_should_return_false_when_actual_is_not_an_AtomicInteger(String actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "");
    // WHEN
    boolean isActualFieldAnAtomicInteger = dualValue.isActualFieldAnAtomicInteger();
    // THEN
    then(isActualFieldAnAtomicInteger).isFalse();
  }

  @Test
  void isExpectedFieldAnAtomicInteger_should_return_true_when_expected_is_an_AtomicInteger() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", new AtomicInteger(123));
    // WHEN
    boolean isExpectedFieldAnAtomicInteger = dualValue.isExpectedFieldAnAtomicInteger();
    // THEN
    then(isExpectedFieldAnAtomicInteger).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "abc" })
  void isExpectedFieldAnAtomicInteger_should_return_false_when_expected_is_not_an_AtomicInteger(String expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected);
    // WHEN
    boolean isExpectedFieldAnAtomicInteger = dualValue.isExpectedFieldAnAtomicInteger();
    // THEN
    then(isExpectedFieldAnAtomicInteger).isFalse();
  }

  // AtomicIntegerArray

  @Test
  void isActualFieldAnAtomicIntegerArray_should_return_true_when_actual_is_an_AtomicIntegerArray() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, new AtomicIntegerArray(new int[] { 1, 2, 3 }), "");
    // WHEN
    boolean isActualFieldAnAtomicIntegerArray = dualValue.isActualFieldAnAtomicIntegerArray();
    // THEN
    then(isActualFieldAnAtomicIntegerArray).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "abc" })
  void isActualFieldAnAtomicIntegerArray_should_return_false_when_actual_is_not_an_AtomicIntegerArray(String actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "");
    // WHEN
    boolean isActualFieldAnAtomicIntegerArray = dualValue.isActualFieldAnAtomicIntegerArray();
    // THEN
    then(isActualFieldAnAtomicIntegerArray).isFalse();
  }

  @Test
  void isExpectedFieldAnAtomicIntegerArray_should_return_true_when_expected_is_an_AtomicIntegerArray() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", new AtomicIntegerArray(new int[] { 1, 2, 3 }));
    // WHEN
    boolean isExpectedFieldAnAtomicIntegerArray = dualValue.isExpectedFieldAnAtomicIntegerArray();
    // THEN
    then(isExpectedFieldAnAtomicIntegerArray).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "abc" })
  void isExpectedFieldAnAtomicIntegerArray_should_return_false_when_expected_is_not_an_AtomicIntegerArray(String expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected);
    // WHEN
    boolean isExpectedFieldAnAtomicIntegerArray = dualValue.isExpectedFieldAnAtomicIntegerArray();
    // THEN
    then(isExpectedFieldAnAtomicIntegerArray).isFalse();
  }

  // AtomicLong

  @Test
  void isActualFieldAnAtomicLong_should_return_true_when_actual_is_an_AtomicLong() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, new AtomicLong(123), "");
    // WHEN
    boolean isActualFieldAnAtomicLong = dualValue.isActualFieldAnAtomicLong();
    // THEN
    then(isActualFieldAnAtomicLong).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "abc" })
  void isActualFieldAnAtomicLong_should_return_false_when_actual_is_not_an_AtomicLong(String actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "");
    // WHEN
    boolean isActualFieldAnAtomicLong = dualValue.isActualFieldAnAtomicLong();
    // THEN
    then(isActualFieldAnAtomicLong).isFalse();
  }

  @Test
  void isExpectedFieldAnAtomicLong_should_return_true_when_expected_is_an_AtomicLong() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", new AtomicLong(123));
    // WHEN
    boolean isExpectedFieldAnAtomicLong = dualValue.isExpectedFieldAnAtomicLong();
    // THEN
    then(isExpectedFieldAnAtomicLong).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "abc" })
  void isExpectedFieldAnAtomicLong_should_return_false_when_expected_is_not_an_AtomicLong(String expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected);
    // WHEN
    boolean isExpectedFieldAnAtomicLong = dualValue.isExpectedFieldAnAtomicLong();
    // THEN
    then(isExpectedFieldAnAtomicLong).isFalse();
  }

  // AtomicLongArray

  @Test
  void isActualFieldAnAtomicLongArray_should_return_true_when_actual_is_an_AtomicLongArray() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, new AtomicLongArray(new long[] { 1, 2, 3 }), "");
    // WHEN
    boolean isActualFieldAnAtomicLongArray = dualValue.isActualFieldAnAtomicLongArray();
    // THEN
    then(isActualFieldAnAtomicLongArray).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "abc" })
  void isActualFieldAnAtomicLongArray_should_return_false_when_actual_is_not_an_AtomicLongArray(String actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "");
    // WHEN
    boolean isActualFieldAnAtomicLongArray = dualValue.isActualFieldAnAtomicLongArray();
    // THEN
    then(isActualFieldAnAtomicLongArray).isFalse();
  }

  @Test
  void isExpectedFieldAnAtomicLongArray_should_return_true_when_expected_is_an_AtomicLongArray() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", new AtomicLongArray(new long[] { 1, 2, 3 }));
    // WHEN
    boolean isExpectedFieldAnAtomicLongArray = dualValue.isExpectedFieldAnAtomicLongArray();
    // THEN
    then(isExpectedFieldAnAtomicLongArray).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "abc" })
  void isExpectedFieldAnAtomicLongArray_should_return_false_when_expected_is_not_an_AtomicLongArray(String expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected);
    // WHEN
    boolean isExpectedFieldAnAtomicLongArray = dualValue.isExpectedFieldAnAtomicLongArray();
    // THEN
    then(isExpectedFieldAnAtomicLongArray).isFalse();
  }

  // AtomicBoolean

  @Test
  void isActualFieldAnAtomicBoolean_should_return_true_when_actual_is_an_AtomicBoolean() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, new AtomicBoolean(true), "");
    // WHEN
    boolean isActualFieldAnAtomicBoolean = dualValue.isActualFieldAnAtomicBoolean();
    // THEN
    then(isActualFieldAnAtomicBoolean).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "abc" })
  void isActualFieldAnAtomicBoolean_should_return_false_when_actual_is_not_an_AtomicBoolean(String actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "");
    // WHEN
    boolean isActualFieldAnAtomicBoolean = dualValue.isActualFieldAnAtomicBoolean();
    // THEN
    then(isActualFieldAnAtomicBoolean).isFalse();
  }

  @Test
  void isExpectedFieldAnAtomicBoolean_should_return_true_when_expected_is_an_AtomicBoolean() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", new AtomicBoolean(true));
    // WHEN
    boolean isExpectedFieldAnAtomicBoolean = dualValue.isExpectedFieldAnAtomicBoolean();
    // THEN
    then(isExpectedFieldAnAtomicBoolean).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "abc" })
  void isExpectedFieldAnAtomicBoolean_should_return_false_when_expected_is_not_an_AtomicBoolean(String expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected);
    // WHEN
    boolean isExpectedFieldAnAtomicBoolean = dualValue.isExpectedFieldAnAtomicBoolean();
    // THEN
    then(isExpectedFieldAnAtomicBoolean).isFalse();
  }

}
