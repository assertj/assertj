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
package org.assertj.core.internal.doublearrays;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.DoubleArrays;
import org.assertj.core.internal.DoubleArraysBaseTest;
import org.junit.jupiter.api.Test;


/**
 * Tests for <code>{@link DoubleArrays#assertHasSameSizeAs(org.assertj.core.api.AssertionInfo, double[], Object[])}</code>.
 * 
 * @author Nicolas François
 * @author Joel Costigliola
 */
public class DoubleArrays_assertHasSameSizeAs_with_Array_Test extends DoubleArraysBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertHasSameSizeAs(someInfo(), null, array("Solo", "Leia", "Luke")))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_size_of_actual_is_not_equal_to_expected_size() {
    AssertionInfo info = someInfo();
    String[] other = array("Solo", "Leia");

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertHasSameSizeAs(info, actual, other))
                                                   .withMessage(shouldHaveSameSizeAs(actual, actual.length,
                                                                                     other.length).create(null,
                                                                                                          info.representation()));
  }

  @Test
  public void should_pass_if_size_of_actual_is_equal_to_expected_size() {
    arrays.assertHasSameSizeAs(someInfo(), actual, array("Solo", "Leia", "Luke"));
  }
}
