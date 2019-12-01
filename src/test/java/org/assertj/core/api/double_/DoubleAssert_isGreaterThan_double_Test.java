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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.double_;

import org.assertj.core.api.DoubleAssert;
import org.assertj.core.api.DoubleAssertBaseTest;
import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;


/**
 * Tests for <code>{@link DoubleAssert#isGreaterThan(double)}</code>.
 * 
 * @author Alex Ruiz
 */
public class DoubleAssert_isGreaterThan_double_Test extends DoubleAssertBaseTest {

  @Override
  protected DoubleAssert invoke_api_method() {
    return assertions.isGreaterThan(6);
  }

  @Override
  protected void verify_internal_effects() {
    verify(doubles).assertGreaterThan(getInfo(assertions), getActual(assertions), 6d);
  }

  @Test
  public void should_fail_with_double_negativeZero() {
    // GIVEN
    final double positiveZero = 0D;
    final double negativeZero = -0D;
    // WHEN
    try {
      assertThat(positiveZero).isGreaterThan(negativeZero);
    } catch (AssertionError e) {
      // THEN
      assertThat(e).hasMessage(format("%nExpecting:%n <0.0f>%nto be greater than:%n <-0.0f> "));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
