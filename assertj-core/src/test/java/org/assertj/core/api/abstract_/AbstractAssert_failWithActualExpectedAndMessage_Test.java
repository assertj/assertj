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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api.abstract_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.BDDAssertions.then;

import org.assertj.core.api.ConcreteAssert;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

/**
 * Tests for <code>AbstractAssert#failWithActualExpectedAndMessage(Object, Object, String, Object...)</code>.
 *
 * @author Joel Costigliola
 * @author Fr Jeremy Krieg
 */
class AbstractAssert_failWithActualExpectedAndMessage_Test {

  private ConcreteAssert assertion;

  private Object actual = "Actual";
  private Object expected = "Expected";

  @BeforeEach
  void setup() {
    assertion = new ConcreteAssert("foo");
  }

  @Test
  void should_fail_with_simple_message() {
    // WHEN
    AssertionFailedError afe = expectAssertionFailedError(() -> assertion.failWithActualExpectedAndMessage(actual, expected,
                                                                                                           "fail"));
    // THEN
    then(afe).hasMessage("fail");
    then(afe.getActual().getEphemeralValue()).isSameAs(actual);
    then(afe.getExpected().getEphemeralValue()).isSameAs(expected);
  }

  @Test
  void should_fail_with_message_having_args() {
    // WHEN
    AssertionFailedError afe = expectAssertionFailedError(() -> assertion.failWithActualExpectedAndMessage(actual, expected,
                                                                                                           "fail %d %s %%s", 5,
                                                                                                           "times"));
    // THEN
    then(afe).hasMessage("fail 5 times %s");
    then(afe.getActual().getEphemeralValue()).isSameAs(actual);
    then(afe.getExpected().getEphemeralValue()).isSameAs(expected);
  }

  @Test
  void should_keep_description_set_by_user() {
    // WHEN
    AssertionFailedError afe = expectAssertionFailedError(() -> assertion.as("user description")
                                                                         .failWithActualExpectedAndMessage(actual, expected,
                                                                                                           "fail %d %s", 5,
                                                                                                           "times"));
    // THEN
    then(afe).hasMessage("[user description] fail 5 times");
    then(afe.getActual().getEphemeralValue()).isSameAs(actual);
    then(afe.getExpected().getEphemeralValue()).isSameAs(expected);
  }

  @Test
  void should_keep_specific_error_message_and_description_set_by_user() {
    // WHEN
    AssertionFailedError afe = expectAssertionFailedError(() -> assertion.as("test context")
                                                                         .overridingErrorMessage("my %d errors %s", 5, "!")
                                                                         .failWithActualExpectedAndMessage(actual, expected,
                                                                                                           "%d %s", 5,
                                                                                                           "time"));
    // THEN
    then(afe).hasMessage("[test context] my 5 errors !");
    then(afe.getActual().getEphemeralValue()).isSameAs(actual);
    then(afe.getExpected().getEphemeralValue()).isSameAs(expected);
  }

  static AssertionFailedError expectAssertionFailedError(ThrowingCallable shouldRaiseAssertionError) {
    AssertionFailedError error = catchThrowableOfType(AssertionFailedError.class, shouldRaiseAssertionError);
    assertThat(error).as("The code under test should have raised an AssertionFailedError").isNotNull();
    return error;
  }

}
