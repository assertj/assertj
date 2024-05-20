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
package org.assertj.core.api.floatarray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withPrecision;
import static org.assertj.core.testkit.FloatArrays.arrayOf;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.FloatArrayAssert;
import org.assertj.core.api.FloatArrayAssertBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link FloatArrayAssert#containsSubsequence(float...)}</code>.
 * 
 * @author Marcin Mikosik
 */
class FloatArrayAssert_containsSubsequence_Test extends FloatArrayAssertBaseTest {

  @Override
  protected FloatArrayAssert invoke_api_method() {
    return assertions.containsSubsequence(6f, 8f);
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertContainsSubsequence(getInfo(assertions), getActual(assertions), arrayOf(6f, 8f));
  }

  @Test
  void should_pass_with_precision_specified_as_last_argument_using_float() {
    // GIVEN
    float[] actual = arrayOf(1.0f, 2.0f, 3.0f);
    // THEN
    assertThat(actual).containsSubsequence(arrayOf(0.91f, 3.09f), withPrecision(0.1f));
  }

  @Test
  void should_pass_with_precision_specified_as_last_argument_using_Float() {
    // GIVEN
    float[] actual = arrayOf(1.0f, 2.0f, 3.0f);
    Float[] expected = { 0.91f, 3.09f };
    // THEN
    assertThat(actual).containsSubsequence(expected, withPrecision(0.1f));
  }

  @Test
  void should_pass_with_precision_specified_in_comparator() {
    // GIVEN
    float[] actual = arrayOf(1.0f, 2.0f, 3.0f);
    // THEN
    assertThat(actual).usingComparatorWithPrecision(0.1f)
                      .containsSubsequence(0.91f, 3.09f);
  }
}
