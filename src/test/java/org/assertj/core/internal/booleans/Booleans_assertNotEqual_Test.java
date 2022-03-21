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
package org.assertj.core.internal.booleans;

import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldNotBeEqual.shouldNotBeEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Booleans;
import org.assertj.core.internal.BooleansBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Booleans#assertNotEqual(AssertionInfo, Boolean, boolean)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Booleans_assertNotEqual_Test extends BooleansBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> booleans.assertNotEqual(someInfo(), null, false))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_bytes_are_not_equal() {
    booleans.assertNotEqual(someInfo(), TRUE, false);
  }

  @Test
  void should_fail_if_bytes_are_equal() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> booleans.assertNotEqual(info, TRUE, true));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotBeEqual(TRUE, true));
  }
}
