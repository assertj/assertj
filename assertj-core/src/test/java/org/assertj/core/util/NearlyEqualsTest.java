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
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class NearlyEqualsTest {

  public static boolean nearlyEqual(float a, float b, float epsilon) {
    final float absA = Math.abs(a);
    final float absB = Math.abs(b);
    final float diff = Math.abs(a - b);

    if (a == b) { // shortcut, handles infinities
      return true;
    } else if (a == 0 || b == 0 || diff < Float.MIN_NORMAL) {
      // a or b is zero or both are extremely close to it
      // relative error is less meaningful here
      float f = epsilon * Float.MIN_NORMAL;
      return diff < f;
    } else { // use relative error
      return diff / Math.min((absA + absB), Float.MAX_VALUE) < epsilon;
    }
  }

  public static boolean nearlyEqual(float a, float b) {
    return nearlyEqual(a, b, 0.00001f);
  }

  /** Regular large numbers - generally not problematic */
  @Test
  void big() {
    assertThat(nearlyEqual(1000000f, 1000001f)).isTrue();
    assertThat(nearlyEqual(1000001f, 1000000f)).isTrue();
    assertThat(nearlyEqual(10000f, 10001f)).isFalse();
    assertThat(nearlyEqual(10001f, 10000f)).isFalse();
  }

  /** Negative large numbers */
  @Test
  void bigNeg() {
    assertThat(nearlyEqual(-1000000f, -1000001f)).isTrue();
    assertThat(nearlyEqual(-1000001f, -1000000f)).isTrue();
    assertThat(nearlyEqual(-10000f, -10001f)).isFalse();
    assertThat(nearlyEqual(-10001f, -10000f)).isFalse();
  }

  /** Numbers around 1 */
  @Test
  void mid() {
    assertThat(nearlyEqual(1.0000001f, 1.0000002f)).isTrue();
    assertThat(nearlyEqual(1.0000002f, 1.0000001f)).isTrue();
    assertThat(nearlyEqual(1.0002f, 1.0001f)).isFalse();
    assertThat(nearlyEqual(1.0001f, 1.0002f)).isFalse();
  }

  /** Numbers around -1 */
  @Test
  void midNeg() {
    assertThat(nearlyEqual(-1.000001f, -1.000002f)).isTrue();
    assertThat(nearlyEqual(-1.000002f, -1.000001f)).isTrue();
    assertThat(nearlyEqual(-1.0001f, -1.0002f)).isFalse();
    assertThat(nearlyEqual(-1.0002f, -1.0001f)).isFalse();
  }

  /** Numbers between 1 and 0 */
  @Test
  void small() {
    assertThat(nearlyEqual(0.000000001000001f, 0.000000001000002f)).isTrue();
    assertThat(nearlyEqual(0.000000001000002f, 0.000000001000001f)).isTrue();
    assertThat(nearlyEqual(0.000000000001002f, 0.000000000001001f)).isFalse();
    assertThat(nearlyEqual(0.000000000001001f, 0.000000000001002f)).isFalse();
  }

  /** Numbers between -1 and 0 */
  @Test
  void smallNeg() {
    assertThat(nearlyEqual(-0.000000001000001f, -0.000000001000002f)).isTrue();
    assertThat(nearlyEqual(-0.000000001000002f, -0.000000001000001f)).isTrue();
    assertThat(nearlyEqual(-0.000000000001002f, -0.000000000001001f)).isFalse();
    assertThat(nearlyEqual(-0.000000000001001f, -0.000000000001002f)).isFalse();
  }

  /** Comparisons involving zero */
  @Test
  void zero() {
    assertThat(nearlyEqual(0.0f, 0.0f)).isTrue();
    assertThat(nearlyEqual(0.0f, -0.0f)).isTrue();
    assertThat(nearlyEqual(-0.0f, -0.0f)).isTrue();
    assertThat(nearlyEqual(0.00000001f, 0.0f)).isFalse();
    assertThat(nearlyEqual(0.0f, 0.00000001f)).isFalse();
    assertThat(nearlyEqual(-0.00000001f, 0.0f)).isFalse();
    assertThat(nearlyEqual(0.0f, -0.00000001f)).isFalse();

    assertThat(nearlyEqual(0.0f, 1e-40f, 0.01f)).isTrue();
    assertThat(nearlyEqual(1e-40f, 0.0f, 0.01f)).isTrue();
    assertThat(nearlyEqual(1e-40f, 0.0f, 0.000001f)).isFalse();
    assertThat(nearlyEqual(0.0f, 1e-40f, 0.000001f)).isFalse();

    assertThat(nearlyEqual(0.0f, -1e-40f, 0.1f)).isTrue();
    assertThat(nearlyEqual(-1e-40f, 0.0f, 0.1f)).isTrue();
    assertThat(nearlyEqual(-1e-40f, 0.0f, 0.00000001f)).isFalse();
    assertThat(nearlyEqual(0.0f, -1e-40f, 0.00000001f)).isFalse();
  }

  /**
   * Comparisons involving extreme values (overflow potential)
   */
  @Test
  void extremeMax() {
    assertThat(nearlyEqual(Float.MAX_VALUE, Float.MAX_VALUE)).isTrue();
    assertThat(nearlyEqual(Float.MAX_VALUE, -Float.MAX_VALUE)).isFalse();
    assertThat(nearlyEqual(-Float.MAX_VALUE, Float.MAX_VALUE)).isFalse();
    assertThat(nearlyEqual(Float.MAX_VALUE, Float.MAX_VALUE / 2)).isFalse();
    assertThat(nearlyEqual(Float.MAX_VALUE, -Float.MAX_VALUE / 2)).isFalse();
    assertThat(nearlyEqual(-Float.MAX_VALUE, Float.MAX_VALUE / 2)).isFalse();
  }

  /**
   * Comparisons involving infinities
   */
  @Test
  void infinities() {
    assertThat(nearlyEqual(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)).isTrue();
    assertThat(nearlyEqual(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY)).isTrue();
    assertThat(nearlyEqual(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY)).isFalse();
    assertThat(nearlyEqual(Float.POSITIVE_INFINITY, Float.MAX_VALUE)).isFalse();
    assertThat(nearlyEqual(Float.NEGATIVE_INFINITY, -Float.MAX_VALUE)).isFalse();
  }

  /**
   * Comparisons involving NaN values
   */
  @Test
  void nan() {
    assertThat(nearlyEqual(Float.NaN, Float.NaN)).isFalse();
    assertThat(nearlyEqual(Float.NaN, 0.0f)).isFalse();
    assertThat(nearlyEqual(-0.0f, Float.NaN)).isFalse();
    assertThat(nearlyEqual(Float.NaN, -0.0f)).isFalse();
    assertThat(nearlyEqual(0.0f, Float.NaN)).isFalse();
    assertThat(nearlyEqual(Float.NaN, Float.POSITIVE_INFINITY)).isFalse();
    assertThat(nearlyEqual(Float.POSITIVE_INFINITY, Float.NaN)).isFalse();
    assertThat(nearlyEqual(Float.NaN, Float.NEGATIVE_INFINITY)).isFalse();
    assertThat(nearlyEqual(Float.NEGATIVE_INFINITY, Float.NaN)).isFalse();
    assertThat(nearlyEqual(Float.NaN, Float.MAX_VALUE)).isFalse();
    assertThat(nearlyEqual(Float.MAX_VALUE, Float.NaN)).isFalse();
    assertThat(nearlyEqual(Float.NaN, -Float.MAX_VALUE)).isFalse();
    assertThat(nearlyEqual(-Float.MAX_VALUE, Float.NaN)).isFalse();
    assertThat(nearlyEqual(Float.NaN, Float.MIN_VALUE)).isFalse();
    assertThat(nearlyEqual(Float.MIN_VALUE, Float.NaN)).isFalse();
    assertThat(nearlyEqual(Float.NaN, -Float.MIN_VALUE)).isFalse();
    assertThat(nearlyEqual(-Float.MIN_VALUE, Float.NaN)).isFalse();
  }

  /** Comparisons of numbers on opposite sides of 0 */
  @Test
  void opposite() {
    assertThat(nearlyEqual(1.000000001f, -1.0f)).isFalse();
    assertThat(nearlyEqual(-1.0f, 1.000000001f)).isFalse();
    assertThat(nearlyEqual(-1.000000001f, 1.0f)).isFalse();
    assertThat(nearlyEqual(1.0f, -1.000000001f)).isFalse();
    assertThat(nearlyEqual(10 * Float.MIN_VALUE, 10 * -Float.MIN_VALUE)).isTrue();
    assertThat(nearlyEqual(10000 * Float.MIN_VALUE, 10000 * -Float.MIN_VALUE)).isFalse();
  }

  /**
   * The really tricky part - comparisons of numbers very close to zero.
   */
  @Test
  void ulp() {
    assertThat(nearlyEqual(Float.MIN_VALUE, Float.MIN_VALUE)).isTrue();
    assertThat(nearlyEqual(Float.MIN_VALUE, -Float.MIN_VALUE)).isTrue();
    assertThat(nearlyEqual(-Float.MIN_VALUE, Float.MIN_VALUE)).isTrue();
    assertThat(nearlyEqual(Float.MIN_VALUE, 0)).isTrue();
    assertThat(nearlyEqual(0, Float.MIN_VALUE)).isTrue();
    assertThat(nearlyEqual(-Float.MIN_VALUE, 0)).isTrue();
    assertThat(nearlyEqual(0, -Float.MIN_VALUE)).isTrue();

    assertThat(nearlyEqual(0.000000001f, -Float.MIN_VALUE)).isFalse();
    assertThat(nearlyEqual(0.000000001f, Float.MIN_VALUE)).isFalse();
    assertThat(nearlyEqual(Float.MIN_VALUE, 0.000000001f)).isFalse();
    assertThat(nearlyEqual(-Float.MIN_VALUE, 0.000000001f)).isFalse();
  }
}
