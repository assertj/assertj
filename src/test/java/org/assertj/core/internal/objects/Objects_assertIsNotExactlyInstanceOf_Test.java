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
package org.assertj.core.internal.objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldNotBeExactlyInstanceOf.shouldNotBeExactlyInstance;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.ObjectsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Objects#assertIsNotExactlyInstanceOf(AssertionInfo, Object, Class)}</code>.
 * 
 * @author Nicolas François
 */
class Objects_assertIsNotExactlyInstanceOf_Test extends ObjectsBaseTest {

  @Test
  void should_pass_if_actual_is_not_exactly_instance_of_type() {
    objects.assertIsNotExactlyInstanceOf(someInfo(), "Yoda", Object.class);
  }

  @Test
  void should_throw_error_if_type_is_null() {
    assertThatNullPointerException().isThrownBy(() -> objects.assertIsNotExactlyInstanceOf(someInfo(), "Yoda", null))
                                    .withMessage("The given type should not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> objects.assertIsNotExactlyInstanceOf(someInfo(), null,
                                                                                                          String.class))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_exactly_instance_of_type() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> objects.assertIsNotExactlyInstanceOf(info, "Yoda", String.class));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotBeExactlyInstance("Yoda", String.class));
  }
}
