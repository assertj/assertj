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
package org.assertj.core.internal.throwables;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldHaveSuppressedException.shouldHaveSuppressedException;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ThrowablesBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Throwables_assertHasSuppressedException_Test extends ThrowablesBaseTest {

  private static final String IAE_EXCEPTION_MESSAGE = "invalid arg";
  private static final String NPE_EXCEPTION_MESSAGE = "null arg";

  private Throwable throwableSuppressedException;

  @BeforeEach
  @Override
  public void setUp() {
    super.setUp();
    throwableSuppressedException = new Throwable();
    throwableSuppressedException.addSuppressed(new IllegalArgumentException(IAE_EXCEPTION_MESSAGE));
    throwableSuppressedException.addSuppressed(new NullPointerException(NPE_EXCEPTION_MESSAGE));
  }

  @Test
  void should_pass_if_one_of_the_suppressed_exception_has_the_expected_type_and_message() {
    throwables.assertHasSuppressedException(someInfo(), throwableSuppressedException,
                                            new IllegalArgumentException(IAE_EXCEPTION_MESSAGE));
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> throwables.assertHasSuppressedException(someInfo(), null,
                                                                                                             new Throwable()))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_suppressed_exception_is_null() {
    assertThatNullPointerException().isThrownBy(() -> throwables.assertHasSuppressedException(someInfo(),
                                                                                              new Throwable(), null))
                                    .withMessage("The expected suppressed exception should not be null");
  }

  @Test
  void should_fail_if_actual_has_no_suppressed_exception_and_expected_suppressed_exception_is_not_null() {
    AssertionInfo info = someInfo();
    Throwable expectedSuppressedException = new Throwable();

    Throwable error = catchThrowable(() -> throwables.assertHasSuppressedException(info, actual, expectedSuppressedException));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldHaveSuppressedException(actual, expectedSuppressedException));
  }

  @Test
  void should_fail_if_suppressed_exception_is_not_instance_of_expected_type() {
    AssertionInfo info = someInfo();
    Throwable expectedSuppressedException = new NullPointerException(IAE_EXCEPTION_MESSAGE);

    Throwable error = catchThrowable(() -> throwables.assertHasSuppressedException(info, throwableSuppressedException,
                                                                                   expectedSuppressedException));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldHaveSuppressedException(throwableSuppressedException, expectedSuppressedException));
  }

  @Test
  void should_fail_if_suppressed_exception_has_not_the_expected_message() {
    AssertionInfo info = someInfo();
    Throwable expectedSuppressedException = new IllegalArgumentException(IAE_EXCEPTION_MESSAGE + "foo");

    Throwable error = catchThrowable(() -> throwables.assertHasSuppressedException(info, throwableSuppressedException,
                                                                                   expectedSuppressedException));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldHaveSuppressedException(throwableSuppressedException, expectedSuppressedException));
  }

  @Test
  void should_fail_if_suppressed_exception_has_no_message_and_the_expected_suppressed_exception_has_one() {
    AssertionInfo info = someInfo();
    Throwable expectedSuppressedException = new IllegalArgumentException("error cause");
    throwableSuppressedException = new Throwable(new IllegalArgumentException());

    Throwable error = catchThrowable(() -> throwables.assertHasSuppressedException(info, throwableSuppressedException,
                                                                                   expectedSuppressedException));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldHaveSuppressedException(throwableSuppressedException, expectedSuppressedException));
  }

  @Test
  void should_fail_if_suppressed_exception_has_different_type_and_message_to_expected_cause() {
    AssertionInfo info = someInfo();
    Throwable expectedSuppressedException = new NullPointerException("error cause");

    Throwable error = catchThrowable(() -> throwables.assertHasSuppressedException(info, throwableSuppressedException,
                                                                                   expectedSuppressedException));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldHaveSuppressedException(throwableSuppressedException, expectedSuppressedException));
  }
}
