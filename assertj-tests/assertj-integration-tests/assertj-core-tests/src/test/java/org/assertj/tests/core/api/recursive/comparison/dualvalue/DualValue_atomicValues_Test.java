/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.recursive.comparison.dualvalue;

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

import org.assertj.core.api.recursive.comparison.DualValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class DualValue_atomicValues_Test {

  private static final List<String> PATH = list("foo", "bar");

  // AtomicReference

  @Test
  void isActualAnAtomicReference_should_return_true_when_actual_is_an_AtomicReference() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, new AtomicReference<>("a"), "");
    // WHEN
    boolean isActualAnAtomicReference = dualValue.isActualAnAtomicReference();
    // THEN
    then(isActualAnAtomicReference).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "abc" })
  void isActualAnAtomicReference_should_return_false_when_actual_is_not_an_AtomicReference(String actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "");
    // WHEN
    boolean isActualAnAtomicReference = dualValue.isActualAnAtomicReference();
    // THEN
    then(isActualAnAtomicReference).isFalse();
  }

  @Test
  void isExpectedAnAtomicReference_should_return_true_when_expected_is_an_AtomicReference() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", new AtomicReference<>("a"));
    // WHEN
    boolean isExpectedAnAtomicReference = dualValue.isExpectedAnAtomicReference();
    // THEN
    then(isExpectedAnAtomicReference).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "abc" })
  void isExpectedAnAtomicReference_should_return_false_when_expected_is_not_an_AtomicReference(String expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected);
    // WHEN
    boolean isExpectedAnAtomicReference = dualValue.isExpectedAnAtomicReference();
    // THEN
    then(isExpectedAnAtomicReference).isFalse();
  }

  // AtomicReferenceArray

  @Test
  void isActualAnAtomicReferenceArray_should_return_true_when_actual_is_an_AtomicReferenceArray() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, new AtomicReferenceArray<>(array("a")), "");
    // WHEN
    boolean isActualAnAtomicReferenceArray = dualValue.isActualAnAtomicReferenceArray();
    // THEN
    then(isActualAnAtomicReferenceArray).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "abc" })
  void isActualAnAtomicReferenceArray_should_return_false_when_actual_is_not_an_AtomicReferenceArray(String actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "");
    // WHEN
    boolean isActualAnAtomicReferenceArray = dualValue.isActualAnAtomicReferenceArray();
    // THEN
    then(isActualAnAtomicReferenceArray).isFalse();
  }

  @Test
  void isExpectedAnAtomicReferenceArray_should_return_true_when_expected_is_an_AtomicReferenceArray() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", new AtomicReferenceArray<>(array("a")));
    // WHEN
    boolean isExpectedAnAtomicReferenceArray = dualValue.isExpectedAnAtomicReferenceArray();
    // THEN
    then(isExpectedAnAtomicReferenceArray).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "abc" })
  void isExpectedAnAtomicReferenceArray_should_return_false_when_expected_is_not_an_AtomicReferenceArray(String expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected);
    // WHEN
    boolean isExpectedAnAtomicReferenceArray = dualValue.isExpectedAnAtomicReferenceArray();
    // THEN
    then(isExpectedAnAtomicReferenceArray).isFalse();
  }

  // AtomicInteger

  @Test
  void isActualAnAtomicInteger_should_return_true_when_actual_is_an_AtomicInteger() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, new AtomicInteger(123), "");
    // WHEN
    boolean isActualAnAtomicInteger = dualValue.isActualAnAtomicInteger();
    // THEN
    then(isActualAnAtomicInteger).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "abc" })
  void isActualAnAtomicInteger_should_return_false_when_actual_is_not_an_AtomicInteger(String actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "");
    // WHEN
    boolean isActualAnAtomicInteger = dualValue.isActualAnAtomicInteger();
    // THEN
    then(isActualAnAtomicInteger).isFalse();
  }

  @Test
  void isExpectedAnAtomicInteger_should_return_true_when_expected_is_an_AtomicInteger() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", new AtomicInteger(123));
    // WHEN
    boolean isExpectedAnAtomicInteger = dualValue.isExpectedAnAtomicInteger();
    // THEN
    then(isExpectedAnAtomicInteger).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "abc" })
  void isExpectedAnAtomicInteger_should_return_false_when_expected_is_not_an_AtomicInteger(String expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected);
    // WHEN
    boolean isExpectedAnAtomicInteger = dualValue.isExpectedAnAtomicInteger();
    // THEN
    then(isExpectedAnAtomicInteger).isFalse();
  }

  // AtomicIntegerArray

  @Test
  void isActualAnAtomicIntegerArray_should_return_true_when_actual_is_an_AtomicIntegerArray() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, new AtomicIntegerArray(new int[] { 1, 2, 3 }), "");
    // WHEN
    boolean isActualAnAtomicIntegerArray = dualValue.isActualAnAtomicIntegerArray();
    // THEN
    then(isActualAnAtomicIntegerArray).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "abc" })
  void isActualAnAtomicIntegerArray_should_return_false_when_actual_is_not_an_AtomicIntegerArray(String actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "");
    // WHEN
    boolean isActualAnAtomicIntegerArray = dualValue.isActualAnAtomicIntegerArray();
    // THEN
    then(isActualAnAtomicIntegerArray).isFalse();
  }

  @Test
  void isExpectedAnAtomicIntegerArray_should_return_true_when_expected_is_an_AtomicIntegerArray() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", new AtomicIntegerArray(new int[] { 1, 2, 3 }));
    // WHEN
    boolean isExpectedAnAtomicIntegerArray = dualValue.isExpectedAnAtomicIntegerArray();
    // THEN
    then(isExpectedAnAtomicIntegerArray).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "abc" })
  void isExpectedAnAtomicIntegerArray_should_return_false_when_expected_is_not_an_AtomicIntegerArray(String expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected);
    // WHEN
    boolean isExpectedAnAtomicIntegerArray = dualValue.isExpectedAnAtomicIntegerArray();
    // THEN
    then(isExpectedAnAtomicIntegerArray).isFalse();
  }

  // AtomicLong

  @Test
  void isActualAnAtomicLong_should_return_true_when_actual_is_an_AtomicLong() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, new AtomicLong(123), "");
    // WHEN
    boolean isActualAnAtomicLong = dualValue.isActualAnAtomicLong();
    // THEN
    then(isActualAnAtomicLong).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "abc" })
  void isActualAnAtomicLong_should_return_false_when_actual_is_not_an_AtomicLong(String actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "");
    // WHEN
    boolean isActualAnAtomicLong = dualValue.isActualAnAtomicLong();
    // THEN
    then(isActualAnAtomicLong).isFalse();
  }

  @Test
  void isExpectedAnAtomicLong_should_return_true_when_expected_is_an_AtomicLong() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", new AtomicLong(123));
    // WHEN
    boolean isExpectedAnAtomicLong = dualValue.isExpectedAnAtomicLong();
    // THEN
    then(isExpectedAnAtomicLong).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "abc" })
  void isExpectedAnAtomicLong_should_return_false_when_expected_is_not_an_AtomicLong(String expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected);
    // WHEN
    boolean isExpectedAnAtomicLong = dualValue.isExpectedAnAtomicLong();
    // THEN
    then(isExpectedAnAtomicLong).isFalse();
  }

  // AtomicLongArray

  @Test
  void isActualAnAtomicLongArray_should_return_true_when_actual_is_an_AtomicLongArray() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, new AtomicLongArray(new long[] { 1, 2, 3 }), "");
    // WHEN
    boolean isActualAnAtomicLongArray = dualValue.isActualAnAtomicLongArray();
    // THEN
    then(isActualAnAtomicLongArray).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "abc" })
  void isActualAnAtomicLongArray_should_return_false_when_actual_is_not_an_AtomicLongArray(String actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "");
    // WHEN
    boolean isActualAnAtomicLongArray = dualValue.isActualAnAtomicLongArray();
    // THEN
    then(isActualAnAtomicLongArray).isFalse();
  }

  @Test
  void isExpectedAnAtomicLongArray_should_return_true_when_expected_is_an_AtomicLongArray() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", new AtomicLongArray(new long[] { 1, 2, 3 }));
    // WHEN
    boolean isExpectedAnAtomicLongArray = dualValue.isExpectedAnAtomicLongArray();
    // THEN
    then(isExpectedAnAtomicLongArray).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "abc" })
  void isExpectedAnAtomicLongArray_should_return_false_when_expected_is_not_an_AtomicLongArray(String expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected);
    // WHEN
    boolean isExpectedAnAtomicLongArray = dualValue.isExpectedAnAtomicLongArray();
    // THEN
    then(isExpectedAnAtomicLongArray).isFalse();
  }

  // AtomicBoolean

  @Test
  void isActualAnAtomicBoolean_should_return_true_when_actual_is_an_AtomicBoolean() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, new AtomicBoolean(true), "");
    // WHEN
    boolean isActualAnAtomicBoolean = dualValue.isActualAnAtomicBoolean();
    // THEN
    then(isActualAnAtomicBoolean).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "abc" })
  void isActualAnAtomicBoolean_should_return_false_when_actual_is_not_an_AtomicBoolean(String actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "");
    // WHEN
    boolean isActualAnAtomicBoolean = dualValue.isActualAnAtomicBoolean();
    // THEN
    then(isActualAnAtomicBoolean).isFalse();
  }

  @Test
  void isExpectedAnAtomicBoolean_should_return_true_when_expected_is_an_AtomicBoolean() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", new AtomicBoolean(true));
    // WHEN
    boolean isExpectedAnAtomicBoolean = dualValue.isExpectedAnAtomicBoolean();
    // THEN
    then(isExpectedAnAtomicBoolean).isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "abc" })
  void isExpectedAnAtomicBoolean_should_return_false_when_expected_is_not_an_AtomicBoolean(String expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "", expected);
    // WHEN
    boolean isExpectedAnAtomicBoolean = dualValue.isExpectedAnAtomicBoolean();
    // THEN
    then(isExpectedAnAtomicBoolean).isFalse();
  }

}
