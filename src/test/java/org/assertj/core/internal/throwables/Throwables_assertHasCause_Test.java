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
package org.assertj.core.internal.throwables;

import static org.assertj.core.error.ShouldHaveCause.shouldHaveCause;
import static org.assertj.core.error.ShouldHaveNoCause.shouldHaveNoCause;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ThrowablesBaseTest;
import org.junit.Test;

public class Throwables_assertHasCause_Test extends ThrowablesBaseTest {

  private static final String EXCEPTION_MESSAGE = "invalid arg";

  private Throwable throwableWithCause = new Throwable(new IllegalArgumentException(EXCEPTION_MESSAGE));

  @Test
  public void should_pass_if_cause_has_expected_type_and_message() {
    throwables.assertHasCause(someInfo(), throwableWithCause, new IllegalArgumentException(EXCEPTION_MESSAGE));
  }

  @Test
  public void should_pass_if_actual_has_no_cause_and_expected_cause_is_null() {
    throwables.assertHasCause(someInfo(), new Throwable(), null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    throwables.assertHasCause(someInfo(), null, new Throwable());
  }

  @Test
  public void should_fail_if_actual_has_no_cause_and_expected_cause_is_not_null() {
    AssertionInfo info = someInfo();
    Throwable expectedCause = new Throwable();
    try {
      throwables.assertHasCause(info, actual, expectedCause);
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldHaveCause(actual.getCause(), expectedCause));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_has_cause_and_expected_cause_is_null() {
    AssertionInfo info = someInfo();
    try {
      throwables.assertHasCause(info, throwableWithCause, null);
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldHaveNoCause(throwableWithCause));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_cause_is_not_instance_of_expected_type() {
    AssertionInfo info = someInfo();
    Throwable expectedCause = new NullPointerException(EXCEPTION_MESSAGE);
    try {
      throwables.assertHasCause(info, throwableWithCause, expectedCause);
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldHaveCause(throwableWithCause.getCause(), expectedCause));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_cause_has_not_the_expected_message() {
    AssertionInfo info = someInfo();
    Throwable expectedCause = new IllegalArgumentException(EXCEPTION_MESSAGE + "foo");
    try {
      throwables.assertHasCause(info, throwableWithCause, expectedCause);
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldHaveCause(throwableWithCause.getCause(), expectedCause));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_cause_has_no_message_and_the_expected_cause_has_one() {
    AssertionInfo info = someInfo();
    Throwable expectedCause = new IllegalArgumentException("error cause");
    throwableWithCause = new Throwable(new IllegalArgumentException());
    try {
      throwables.assertHasCause(info, throwableWithCause, expectedCause);
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldHaveCause(throwableWithCause.getCause(), expectedCause));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_cause_has_different_type_and_message_to_expected_cause() {
    AssertionInfo info = someInfo();
    Throwable expectedCause = new NullPointerException("error cause");
    try {
      throwables.assertHasCause(info, throwableWithCause, expectedCause);
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldHaveCause(throwableWithCause.getCause(), expectedCause));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
