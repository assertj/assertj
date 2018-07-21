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
package org.assertj.core.internal.throwables;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldHaveCauseExactlyInstance.shouldHaveCauseExactlyInstance;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ThrowablesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * {@link org.assertj.core.internal.Throwables#assertHasCauseExactlyInstanceOf(org.assertj.core.api.AssertionInfo, Throwable, Class)}
 * .
 * 
 * @author Jean-Christophe Gay
 */
public class Throwables_assertHasCauseExactlyInstanceOf_Test extends ThrowablesBaseTest {

  private Throwable throwableWithCause = new Throwable(new IllegalArgumentException());

  @Test
  public void should_pass_if_cause_is_exactly_instance_of_expected_type() {
    throwables.assertHasCauseExactlyInstanceOf(someInfo(), throwableWithCause, IllegalArgumentException.class);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> throwables.assertHasCauseExactlyInstanceOf(someInfo(), null, IllegalArgumentException.class))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_throw_NullPointerException_if_given_type_is_null() {
    assertThatNullPointerException().isThrownBy(() -> throwables.assertHasCauseExactlyInstanceOf(someInfo(),
                                                                                                 throwableWithCause,
                                                                                                 null))
                                    .withMessage("The given type should not be null");
  }

  @Test
  public void should_fail_if_actual_has_no_cause() {
    AssertionInfo info = someInfo();
    Class<NullPointerException> expectedCauseType = NullPointerException.class;
    try {
      throwables.assertHasCauseExactlyInstanceOf(info, actual, expectedCauseType);
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldHaveCauseExactlyInstance(actual, expectedCauseType));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }  

  @Test
  public void should_fail_if_cause_is_not_instance_of_expected_type() {
    AssertionInfo info = someInfo();
    Class<NullPointerException> expectedCauseType = NullPointerException.class;
    try {
      throwables.assertHasCauseExactlyInstanceOf(info, throwableWithCause, expectedCauseType);
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldHaveCauseExactlyInstance(throwableWithCause, expectedCauseType));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_cause_is_not_exactly_instance_of_expected_type() {
    AssertionInfo info = someInfo();
    Class<RuntimeException> expectedCauseType = RuntimeException.class;
    try {
      throwables.assertHasCauseExactlyInstanceOf(info, throwableWithCause, expectedCauseType);
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldHaveCauseExactlyInstance(throwableWithCause, expectedCauseType));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
