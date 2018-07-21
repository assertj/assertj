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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.floatarray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withPrecision;
import static org.assertj.core.test.FloatArrays.arrayOf;

import org.assertj.core.api.FloatArrayAssert;
import org.assertj.core.api.FloatArrayAssertBaseTest;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.verify;


/**
 * Tests for <code>{@link FloatArrayAssert#doesNotContain(float...)}</code>.
 * 
 * @author Alex Ruiz
 */
public class FloatArrayAssert_doesNotContain_Test extends FloatArrayAssertBaseTest {

  @Override
  protected FloatArrayAssert invoke_api_method() {
    return assertions.doesNotContain(6f, 8f);
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertDoesNotContain(getInfo(assertions), getActual(assertions), arrayOf(6f, 8f));
  }

  @Test
  public void should_pass_with_precision_specified_as_last_argument() {
    // GIVEN
    float[] actual = arrayOf(1.0f, 2.0f, 3.0f);
    // THEN
    assertThat(actual).doesNotContain(arrayOf(1.1f, 2.1f), withPrecision(0.0001f));
  }

  @Test
  public void should_pass_with_precision_specified_in_comparator() {
    // GIVEN
    float[] actual = arrayOf(1.0f, 2.0f);
    // THEN
    assertThat(actual).usingComparatorWithPrecision(0.01f)
                      .doesNotContain(1.1f, 2.1f);
  }
}
