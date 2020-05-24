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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Double2DArrays;
import org.assertj.core.internal.Double2DArraysBaseTest;
import org.junit.jupiter.api.Test;


/**
 * Tests for <code>{@link Double2DArrays#assertNotEmpty(AssertionInfo, double[][])}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Maciej Wajcht
 */
public class Double2DArrays_assertNotEmpty_Test extends Double2DArraysBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertNotEmpty(someInfo(), null))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_is_empty() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> arrays.assertNotEmpty(info, new double[][] {}));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotBeEmpty());
  }

  @Test
  public void should_pass_if_actual_is_not_empty() {
    arrays.assertNotEmpty(someInfo(), new double[][] {{1.0}, {2.0}});
  }
}
