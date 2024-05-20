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
package org.assertj.core.internal;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author Joel Costigliola
 */
class StandardComparisonStrategy_areEqual_Test {

  private final StandardComparisonStrategy underTest = StandardComparisonStrategy.instance();

  @Test
  void should_return_true_if_both_actual_and_other_are_null() {
    // WHEN
    boolean result = underTest.areEqual(null, null);
    // THEN
    then(result).isTrue();
  }

  @Test
  void should_return_false_if_actual_is_null_and_other_is_not() {
    // GIVEN
    Object other = new Object();
    // WHEN
    boolean result = underTest.areEqual(null, other);
    // THEN
    then(result).isFalse();
  }

  @Test
  void should_return_true_if_Object_arrays_are_equal() {
    // GIVEN
    Object[] actual = { "Luke", "Yoda", "Leia" };
    Object[] other = { "Luke", "Yoda", "Leia" };
    // WHEN
    boolean result = underTest.areEqual(actual, other);
    // THEN
    then(result).isTrue();
  }

  @Test
  void should_return_false_if_Object_arrays_are_not_equal() {
    // GIVEN
    Object[] actual = { "Luke", "Leia", "Yoda" };
    Object[] other = { "Luke", "Yoda", "Leia" };
    // WHEN
    boolean result = underTest.areEqual(actual, other);
    // THEN
    then(result).isFalse();
  }

  @Test
  void should_return_true_if_byte_arrays_are_equal() {
    // GIVEN
    byte[] actual = { 1, 2, 3 };
    byte[] other = { 1, 2, 3 };
    // WHEN
    boolean result = underTest.areEqual(actual, other);
    // THEN
    then(result).isTrue();
  }

  @Test
  void should_return_false_if_byte_arrays_are_not_equal() {
    // GIVEN
    byte[] actual = { 1, 2, 3 };
    byte[] other = { 4, 5, 6 };
    // WHEN
    boolean result = underTest.areEqual(actual, other);
    // THEN
    then(result).isFalse();
  }

  @Test
  void should_return_true_if_short_arrays_are_equal() {
    // GIVEN
    short[] actual = { 1, 2, 3 };
    short[] other = { 1, 2, 3 };
    // WHEN
    boolean result = underTest.areEqual(actual, other);
    // THEN
    then(result).isTrue();
  }

  @Test
  void should_return_false_if_short_arrays_are_not_equal() {
    // GIVEN
    short[] actual = { 1, 2, 3 };
    short[] other = { 4, 5, 6 };
    // WHEN
    boolean result = underTest.areEqual(actual, other);
    // THEN
    then(result).isFalse();
  }

  @Test
  void should_return_true_if_int_arrays_are_equal() {
    // GIVEN
    int[] actual = { 1, 2, 3 };
    int[] other = { 1, 2, 3 };
    // WHEN
    boolean result = underTest.areEqual(actual, other);
    // THEN
    then(result).isTrue();
  }

  @Test
  void should_return_false_if_int_arrays_are_not_equal() {
    // GIVEN
    int[] actual = { 1, 2, 3 };
    int[] other = { 4, 5, 6 };
    // WHEN
    boolean result = underTest.areEqual(actual, other);
    // THEN
    then(result).isFalse();
  }

  @Test
  void should_return_true_if_long_arrays_are_equal() {
    // GIVEN
    long[] actual = { 1L, 2L, 3L };
    long[] other = { 1L, 2L, 3L };
    // WHEN
    boolean result = underTest.areEqual(actual, other);
    // THEN
    then(result).isTrue();
  }

  @Test
  void should_return_false_if_long_arrays_are_not_equal() {
    // GIVEN
    long[] actual = { 1L, 2L, 3L };
    long[] other = { 4L, 5L, 6L };
    // WHEN
    boolean result = underTest.areEqual(actual, other);
    // THEN
    then(result).isFalse();
  }

  @Test
  void should_return_true_if_char_arrays_are_equal() {
    // GIVEN
    char[] actual = { '1', '2', '3' };
    char[] other = { '1', '2', '3' };
    // WHEN
    boolean result = underTest.areEqual(actual, other);
    // THEN
    then(result).isTrue();
  }

  @Test
  void should_return_false_if_char_arrays_are_not_equal() {
    // GIVEN
    char[] actual = { '1', '2', '3' };
    char[] other = { '4', '5', '6' };
    // WHEN
    boolean result = underTest.areEqual(actual, other);
    // THEN
    then(result).isFalse();
  }

  @Test
  void should_return_true_if_float_arrays_are_equal() {
    // GIVEN
    float[] actual = { 1.0f, 2.0f, 3.0f };
    float[] other = { 1.0f, 2.0f, 3.0f };
    // WHEN
    boolean result = underTest.areEqual(actual, other);
    // THEN
    then(result).isTrue();
  }

  @Test
  void should_return_false_if_float_arrays_are_not_equal() {
    // GIVEN
    float[] actual = { 1.0f, 2.0f, 3.0f };
    float[] other = { 4.0f, 5.0f, 6.0f };
    // WHEN
    boolean result = underTest.areEqual(actual, other);
    // THEN
    then(result).isFalse();
  }

  @Test
  void should_return_true_if_double_arrays_are_equal() {
    // GIVEN
    double[] actual = { 1.0, 2.0, 3.0 };
    double[] other = { 1.0, 2.0, 3.0 };
    // WHEN
    boolean result = underTest.areEqual(actual, other);
    // THEN
    then(result).isTrue();
  }

  @Test
  void should_return_false_if_double_arrays_are_not_equal() {
    // GIVEN
    double[] actual = { 1.0, 2.0, 3.0 };
    double[] other = { 4.0, 5.0, 6.0 };
    // WHEN
    boolean result = underTest.areEqual(actual, other);
    // THEN
    then(result).isFalse();
  }

  @Test
  void should_return_true_if_boolean_arrays_are_equal() {
    // GIVEN
    boolean[] actual = { true, false };
    boolean[] other = { true, false };
    // WHEN
    boolean result = underTest.areEqual(actual, other);
    // THEN
    then(result).isTrue();
  }

  @Test
  void should_return_false_if_boolean_arrays_are_not_equal() {
    // GIVEN
    boolean[] actual = { true, false };
    boolean[] other = { false, true };
    // WHEN
    boolean result = underTest.areEqual(actual, other);
    // THEN
    then(result).isFalse();
  }

  @ParameterizedTest
  @MethodSource("arrays")
  void should_return_false_if_array_is_non_null_and_other_is_null(Object actual) {
    // WHEN
    boolean result = underTest.areEqual(actual, null);
    // THEN
    then(result).isFalse();
  }

  private static Stream<Object> arrays() {
    return Stream.of(argument(new Object[] { "Luke", "Yoda", "Leia" }),
                     new byte[] { 1, 2, 3 },
                     new short[] { 1, 2, 3 },
                     new int[] { 1, 2, 3 },
                     new long[] { 1L, 2L, 3L },
                     new char[] { '1', '2', '3' },
                     new float[] { 1.0f, 2.0f, 3.0f },
                     new double[] { 1.0, 2.0, 3.0 },
                     new boolean[] { true, false });
  }

  // Arrays of objects require additional wrapping to avoid expanding into individual elements.
  // See https://github.com/junit-team/junit5/issues/1665 and https://github.com/junit-team/junit5/issues/2708
  private static Arguments argument(Object[] array) {
    return () -> new Object[] { array };
  }

  @Test
  void should_fail_if_equals_implementation_fails() {
    // GIVEN
    Object actual = new EqualsThrowsException();
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.areEqual(actual, new Object()));
    // THEN
    then(thrown).isInstanceOf(RuntimeException.class)
                .hasMessage("Boom!");
  }

  private static class EqualsThrowsException {

    @Override
    public boolean equals(Object obj) {
      throw new RuntimeException("Boom!");
    }

  }

  @ParameterizedTest
  @MethodSource({ "correctEquals", "contractViolatingEquals" })
  void should_delegate_to_equals_implementation_if_actual_is_not_null(Object actual, Object other, boolean expected) {
    // WHEN
    boolean result = underTest.areEqual(actual, other);
    // THEN
    then(result).isEqualTo(expected);
  }

  // not part of contractViolatingEquals due to test order dependency
  @Test
  void should_delegate_to_inconsistent_equals_implementation() {
    // GIVEN
    Object actual = new NonConsistent();
    // WHEN
    boolean[] results = {
        underTest.areEqual(actual, actual),
        underTest.areEqual(actual, actual),
        underTest.areEqual(actual, actual)
    };
    // THEN
    then(results).containsExactly(true, false, true);
  }

  private static Stream<Arguments> correctEquals() {
    Object object = new Object();

    return Stream.of(arguments(object, null, false),
                     arguments(object, object, true),
                     arguments(object, new Object(), false),
                     arguments("Luke", null, false),
                     arguments("Luke", "Luke", true),
                     arguments("Luke", "Yoda", false));
  }

  private static Stream<Arguments> contractViolatingEquals() {
    AlwaysTrue alwaysTrue = new AlwaysTrue();
    AlwaysFalse alwaysFalse = new AlwaysFalse();

    NonReflexive nonReflexiveX = new NonReflexive();

    NonSymmetric nonSymmetricY = new NonSymmetric(null);
    NonSymmetric nonSymmetricX = new NonSymmetric(nonSymmetricY);

    NonTransitive nonTransitiveZ = new NonTransitive(null, null);
    NonTransitive nonTransitiveY = new NonTransitive(nonTransitiveZ, null);
    NonTransitive nonTransitiveX = new NonTransitive(nonTransitiveY, nonTransitiveZ);

    return Stream.of(arguments(alwaysTrue, null, true),
                     arguments(alwaysFalse, alwaysFalse, false),
                     arguments(nonReflexiveX, nonReflexiveX, false),
                     arguments(nonSymmetricX, nonSymmetricY, true),
                     arguments(nonSymmetricY, nonSymmetricX, false),
                     arguments(nonTransitiveX, nonTransitiveY, true),
                     arguments(nonTransitiveY, nonTransitiveZ, true),
                     arguments(nonTransitiveX, nonTransitiveZ, false));
  }

  private static class AlwaysTrue {

    @Override
    public boolean equals(Object obj) {
      return true;
    }

    @Override
    public String toString() {
      return "always true";
    }

  }

  private static class AlwaysFalse {

    @Override
    public boolean equals(Object obj) {
      return false;
    }

    @Override
    public String toString() {
      return "always false";
    }

  }

  private static class NonReflexive {

    @Override
    public boolean equals(Object obj) {
      return this != obj;
    }

    @Override
    public String toString() {
      return "non reflexive";
    }

  }

  private static class NonSymmetric {

    private final Object other;

    NonSymmetric(Object other) {
      this.other = other;
    }

    @Override
    public boolean equals(Object obj) {
      return obj == other;
    }

    @Override
    public String toString() {
      return "non symmetric";
    }

  }

  private static class NonTransitive {

    private final Object y, z;

    NonTransitive(Object y, Object z) {
      this.y = y;
      this.z = z;
    }

    @Override
    public boolean equals(Object obj) {
      return obj == y || obj != z;
    }

    @Override
    public String toString() {
      return "non transitive";
    }

  }

  private static class NonConsistent {

    private int i = 0;

    @Override
    public boolean equals(Object obj) {
      return (++i % 2) != 0;
    }

    @Override
    public String toString() {
      return "non consistent";
    }

  }

}
