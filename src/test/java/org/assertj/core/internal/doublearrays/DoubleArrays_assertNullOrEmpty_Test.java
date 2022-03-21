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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.internal.doublearrays;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldBeNullOrEmpty.shouldBeNullOrEmpty;
import static org.assertj.core.test.DoubleArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.DoubleArrays;
import org.assertj.core.internal.DoubleArraysBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link DoubleArrays#assertNullOrEmpty(AssertionInfo, double[])}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class DoubleArrays_assertNullOrEmpty_Test extends DoubleArraysBaseTest {

  @Test
  void should_fail_if_array_is_not_null_and_is_not_empty() {
    double[] actual = { 6d, 8d };
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertNullOrEmpty(someInfo(), actual))
                                                   .withMessage(shouldBeNullOrEmpty(actual).create());
  }

  @Test
  void should_pass_if_array_is_null() {
    arrays.assertNullOrEmpty(someInfo(), null);
  }

  @Test
  void should_pass_if_array_is_empty() {
    arrays.assertNullOrEmpty(someInfo(), emptyArray());
  }
}
