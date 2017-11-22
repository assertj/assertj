/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.FloatComparator.elaborateFloatComparator;

import org.assertj.core.api.AbstractBooleanAssert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;

@RunWith(DataProviderRunner.class)
public class ElaborateFloatComparatorTest {

  private static final Float DEFAULT_PRECISION = 0.00001f;
  private static final String ASSERTION_DESC = "comparing %f to %f with an elaborate algorithm set with a precision of %s";
  private static final FloatComparator DEFAULT_COMPARATOR = elaborateFloatComparator(DEFAULT_PRECISION);

  @Test
  @DataProvider({
      "1.0, 1.0",
      "1.00001, 1.0",
      "1.0, 1.00001",
      "0.00001, 0.0",
      "0.0, 0.00001",
      "-1.00001, -1.0",
      "-1.0, -1.00001",
      "null, null"
  })
  public void should_be_equal_if_difference_is_less_than_or_equal_to_epsilon(Float a, Float b) {
    assertNearlyEqual(a, b);
  }

  @Test
  @DataProvider({
      "1.0, 2.0",
      "1.010001, 1.0",
      "1.0, 1.010001",
      "0.01, 0.0",
      "0.0, 0.010001",
      "-1.010001, -1.0",
      "-1.0, -1.010001",
      "null, 1.0",
      "1.0, null"
  })
  public void should_not_be_equal_if_difference_is_more_than_epsilon(Float a, Float b) {
    assertNotNearlyEqual(a, b);
  }

  /** Regular large numbers - generally not problematic */
  @Test
  public void big() {
    assertNearlyEqual(1000000f, 1000001f);
    assertNearlyEqual(1000001f, 1000000f);
    assertNotNearlyEqual(10000f, 10001f);
    assertNotNearlyEqual(10001f, 10000f);
  }

  /** Negative large numbers */
  @Test
  public void bigNeg() {
    assertNearlyEqual(-1000000f, -1000001f);
    assertNearlyEqual(-1000001f, -1000000f);
    assertNotNearlyEqual(-10000f, -10001f);
    assertNotNearlyEqual(-10001f, -10000f);
  }

  /** Numbers around 1 */
  @Test
  public void mid() {
    assertNearlyEqual(1.0000001f, 1.0000002f);
    assertNearlyEqual(1.0000002f, 1.0000001f);
    assertNotNearlyEqual(1.0002f, 1.0001f);
    assertNotNearlyEqual(1.0001f, 1.0002f);
  }

  /** Numbers around -1 */
  @Test
  public void midNeg() {
    assertNearlyEqual(-1.000001f, -1.000002f);
    assertNearlyEqual(-1.000002f, -1.000001f);
    assertNotNearlyEqual(-1.0001f, -1.0002f);
    assertNotNearlyEqual(-1.0002f, -1.0001f);
  }

  /** Numbers between 1 and 0 */
  @Test
  public void small() {
    assertNearlyEqual(0.000000001000001f, 0.000000001000002f);
    assertNearlyEqual(0.000000001000002f, 0.000000001000001f);
    assertNotNearlyEqual(0.000000000001002f, 0.000000000001001f);
    assertNotNearlyEqual(0.000000000001001f, 0.000000000001002f);
  }

  /** Numbers between -1 and 0 */
  @Test
  public void smallNeg() {
    assertNearlyEqual(-0.000000001000001f, -0.000000001000002f);
    assertNearlyEqual(-0.000000001000002f, -0.000000001000001f);
    assertNotNearlyEqual(-0.000000000001002f, -0.000000000001001f);
    assertNotNearlyEqual(-0.000000000001001f, -0.000000000001002f);
  }

  /** Small differences away from zero */
  @Test
  public void smallDiffs() {
    assertNearlyEqual(0.3f, 0.30000003f);
    assertNearlyEqual(-0.3f, -0.30000003f);
  }

  /** Comparisons involving zero */
  @Test
  public void zero() {
    assertNearlyEqual(0.0f, 0.0f);
    assertNearlyEqual(0.0f, -0.0f);
    assertNearlyEqual(-0.0f, -0.0f);
    assertNotNearlyEqual(0.00000001f, 0.0f);
    assertNotNearlyEqual(0.0f, 0.00000001f);
    assertNotNearlyEqual(-0.00000001f, 0.0f);
    assertNotNearlyEqual(0.0f, -0.00000001f);

    assertNearlyEqual(0.0f, 1e-40f, 0.01f);
    assertNearlyEqual(1e-40f, 0.0f, 0.01f);
    assertNotNearlyEqual(1e-40f, 0.0f, 0.000001f);
    assertNotNearlyEqual(0.0f, 1e-40f, 0.000001f);

    assertNearlyEqual(0.0f, -1e-40f, 0.1f);
    assertNearlyEqual(-1e-40f, 0.0f, 0.1f);
    assertNotNearlyEqual(-1e-40f, 0.0f, 0.00000001f);
    assertNotNearlyEqual(0.0f, -1e-40f, 0.00000001f);
  }

  /**
   * Comparisons involving extreme values (overflow potential)
   */
  @Test
  public void extremeMax() {
    assertNearlyEqual(Float.MAX_VALUE, Float.MAX_VALUE);
    assertNotNearlyEqual(Float.MAX_VALUE, -Float.MAX_VALUE);
    assertNotNearlyEqual(-Float.MAX_VALUE, Float.MAX_VALUE);
    assertNotNearlyEqual(Float.MAX_VALUE, Float.MAX_VALUE / 2);
    assertNotNearlyEqual(Float.MAX_VALUE, -Float.MAX_VALUE / 2);
    assertNotNearlyEqual(-Float.MAX_VALUE, Float.MAX_VALUE / 2);
  }

  /**
   * Comparisons involving infinities
   */
  @Test
  public void infinities() {
    assertNearlyEqual(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
    assertNearlyEqual(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
    assertNotNearlyEqual(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
    assertNotNearlyEqual(Float.POSITIVE_INFINITY, Float.MAX_VALUE);
    assertNotNearlyEqual(Float.NEGATIVE_INFINITY, -Float.MAX_VALUE);
  }

  /**
   * Comparisons involving NaN values
   */
  @Test
  public void nan() {
    assertNotNearlyEqual(Float.NaN, Float.NaN);
    assertNotNearlyEqual(Float.NaN, 0.0f);
    assertNotNearlyEqual(-0.0f, Float.NaN);
    assertNotNearlyEqual(Float.NaN, -0.0f);
    assertNotNearlyEqual(0.0f, Float.NaN);
    assertNotNearlyEqual(Float.NaN, Float.POSITIVE_INFINITY);
    assertNotNearlyEqual(Float.POSITIVE_INFINITY, Float.NaN);
    assertNotNearlyEqual(Float.NaN, Float.NEGATIVE_INFINITY);
    assertNotNearlyEqual(Float.NEGATIVE_INFINITY, Float.NaN);
    assertNotNearlyEqual(Float.NaN, Float.MAX_VALUE);
    assertNotNearlyEqual(Float.MAX_VALUE, Float.NaN);
    assertNotNearlyEqual(Float.NaN, -Float.MAX_VALUE);
    assertNotNearlyEqual(-Float.MAX_VALUE, Float.NaN);
    assertNotNearlyEqual(Float.NaN, Float.MIN_VALUE);
    assertNotNearlyEqual(Float.MIN_VALUE, Float.NaN);
    assertNotNearlyEqual(Float.NaN, -Float.MIN_VALUE);
    assertNotNearlyEqual(-Float.MIN_VALUE, Float.NaN);
  }

  /** Comparisons of numbers on opposite sides of 0 */
  @Test
  public void opposite() {
    assertNotNearlyEqual(1.000000001f, -1.0f);
    assertNotNearlyEqual(-1.0f, 1.000000001f);
    assertNotNearlyEqual(-1.000000001f, 1.0f);
    assertNotNearlyEqual(1.0f, -1.000000001f);
    assertNearlyEqual(10 * Float.MIN_VALUE, 10 * -Float.MIN_VALUE);
    assertNotNearlyEqual(10000 * Float.MIN_VALUE, 10000 * -Float.MIN_VALUE);
  }

  /**
   * The really tricky part - comparisons of numbers very close to zero.
   */
  @Test
  public void ulp() {
    assertNearlyEqual(Float.MIN_VALUE, Float.MIN_VALUE);
    assertNearlyEqual(Float.MIN_VALUE, -Float.MIN_VALUE);
    assertNearlyEqual(-Float.MIN_VALUE, Float.MIN_VALUE);
    assertNearlyEqual(Float.MIN_VALUE, 0.0f);
    assertNearlyEqual(0.0f, Float.MIN_VALUE);
    assertNearlyEqual(-Float.MIN_VALUE, 0.0f);
    assertNearlyEqual(0.0f, -Float.MIN_VALUE);

    assertNotNearlyEqual(0.000000001f, -Float.MIN_VALUE);
    assertNotNearlyEqual(0.000000001f, Float.MIN_VALUE);
    assertNotNearlyEqual(Float.MIN_VALUE, 0.000000001f);
    assertNotNearlyEqual(-Float.MIN_VALUE, 0.000000001f);
  }

  private static void assertNearlyEqual(Float x, Float y) {
    assertNearlyEqual(x, y, DEFAULT_PRECISION);
  }

  private static void assertNotNearlyEqual(Float x, Float y) {
    assertNotNearlyEqual(x, y, DEFAULT_PRECISION);
  }

  private static void assertNearlyEqual(Float x, Float y, Float precision) {
    nearlyEqualAssertion(x, y, precision).isTrue();
  }

  private static void assertNotNearlyEqual(Float x, Float y, Float precision) {
    nearlyEqualAssertion(x, y, precision).isFalse();
  }

  private static AbstractBooleanAssert<?> nearlyEqualAssertion(Float x, Float y, Float precision) {
    FloatComparator elaborateFloatComparator = elaborateComparator(precision);
    return assertThat(elaborateFloatComparator.compare(x, y) == 0).as(ASSERTION_DESC, x, y, precision);
  }

  private static FloatComparator elaborateComparator(Float precision) {
    return precision == DEFAULT_PRECISION ? DEFAULT_COMPARATOR : elaborateFloatComparator(precision);
  }

}
