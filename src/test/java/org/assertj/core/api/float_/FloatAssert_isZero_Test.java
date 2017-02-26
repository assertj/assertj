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
package org.assertj.core.api.float_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.FloatAssert;
import org.assertj.core.api.FloatAssertBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link FloatAssert#isZero()}</code>.
 * 
 * @author Alex Ruiz
 */
public class FloatAssert_isZero_Test extends FloatAssertBaseTest {

  @Override
  protected FloatAssert invoke_api_method() {
    return assertions.isZero();
  }

  @Override
  protected void verify_internal_effects() {
    verify(floats).assertIsZero(getInfo(assertions), getActual(assertions));
  }

  @Test
  public void should_pass_with_primitive_negative_zero() {
    // GIVEN
    final float negativeZero = -0.0f;
    // THEN
    assertThat(negativeZero).isZero();
  }

  @Test
  public void should_pass_with_primitive_positive_zero() {
    // GIVEN
    final float positiveZero = 0.0f;
    // THEN
    assertThat(positiveZero).isZero();
  }

  @Test
  public void should_pass_with_Float_positive_zero() {
    // GIVEN
    final Float positiveZero = 0.0f;
    // THEN
    assertThat(positiveZero).isZero();
  }

  @Test
  public void should_fail_with_non_zero() {
    // GIVEN
    final float notZero = 1.0f;
    try {
      // WHEN
      assertThat(notZero).isZero();
    } catch (AssertionError e) {
      // THEN
      assertThat(e).hasMessage("expected:<[0].0f> but was:<[1].0f>");
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_with_Float_negative_zero() {
    // GIVEN
    final Float negativeZero = -0.0f;
    try {
      // WHEN
      assertThat(negativeZero).isZero();
    } catch (AssertionError e) {
      // THEN
      assertThat(e).hasMessage("expected:<[]0.0f> but was:<[-]0.0f>");
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
