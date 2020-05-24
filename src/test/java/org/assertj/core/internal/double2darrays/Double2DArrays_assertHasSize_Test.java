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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.internal.double2darrays;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldHaveSize.shouldHaveSize;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Double2DArrays;
import org.assertj.core.internal.Double2DArraysBaseTest;
import org.junit.jupiter.api.Test;


/**
 * Tests for <code>{@link Double2DArrays#assertHasSize(AssertionInfo, double[][], int, int)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Maciej Wajcht
 */
public class Double2DArrays_assertHasSize_Test extends Double2DArraysBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertHasSize(someInfo(), null, 2, 3))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_first_dimension_size_of_actual_is_not_equal_to_expected_size() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertHasSize(someInfo(), actual, 10, 3))
                                                   .withMessage(shouldHaveSize(actual, actual.length, 10).create());
  }

  @Test
  public void should_fail_if_second_dimension_size_of_actual_is_not_equal_to_expected_size() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertHasSize(someInfo(), actual, 2, 10))
      .withMessage(shouldHaveSize(new double[] {0.0, 2.0, 4.0}, 3, 10, 0).create());
  }

  @Test
  public void should_pass_if_size_of_actual_is_equal_to_expected_size() {
    arrays.assertHasSize(someInfo(), actual, 2, 3);
  }
}
