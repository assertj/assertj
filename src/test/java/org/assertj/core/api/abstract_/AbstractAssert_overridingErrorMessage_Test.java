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
package org.assertj.core.api.abstract_;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;

import java.util.function.Supplier;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.ConcreteAssert;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link AbstractAssert#overridingErrorMessage(String, Object...)}</code>.
 * Tests for <code>{@link AbstractAssert#overridingErrorMessage(Supplier)}</code>.
 *
 * @author Joel Costigliola
 */
public class AbstractAssert_overridingErrorMessage_Test {

  private ConcreteAssert assertions;

  @BeforeEach
  public void setUp() {
    assertions = new ConcreteAssert(6L);
  }

  @Test
  public void should_return_this() {
    then(assertions.overridingErrorMessage("")).isSameAs(assertions);
  }

  @Test
  public void should_pass_with_error_message_overridden() {
    assertions.overridingErrorMessage("new error message").isEqualTo(6L);
  }

  @Test
  public void should_fail_with_overridden_error_message() {
    // GIVEN
    ThrowingCallable code = () -> assertions.overridingErrorMessage("new error message")
                                            .isEqualTo(8L);
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage("new error message");
  }

  @Test
  public void should_fail_with_overridden_error_message_not_interpreted_with_string_format_feature_as_no_args_are_given() {
    // GIVEN
    ThrowingCallable code = () -> assertions.overridingErrorMessage("new error message with special character like (%)")
                                            .isEqualTo(8L);
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(format("new error message with special character like (%%)"));
  }

  @Test
  public void should_fail_with_overridden_error_message_interpreted_with_string_format_feature() {
    // GIVEN
    long expected = 8L;
    ThrowingCallable code = () -> assertions.overridingErrorMessage("new error message, expected value was : '%s'", expected)
                                            .isEqualTo(expected);
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage("new error message, expected value was : '8'");
  }

  @Test
  public void should_fail_with_description_and_overridden_error_message_using_string_format_feature() {
    // GIVEN
    long expected = 8L;
    ThrowingCallable code = () -> assertions.as("test")
                                            .overridingErrorMessage("new error message, expected value was : '%s'", expected)
                                            .isEqualTo(expected);
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage("[test] new error message, expected value was : '8'");
  }

  @Test
  public void should_fail_with_overridden_error_message_interpreted_using_supplier() {
    // GIVEN
    long expected = 8L;
    // WHEN
    ThrowingCallable code = () -> assertions.overridingErrorMessage(() -> format("new error message, expected value was : '%s'",
                                                                                 expected))
                                            .isEqualTo(expected);
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage("new error message, expected value was : '8'");
  }

  @Test
  public void should_fail_with_description_and_overridden_error_message_using_supplier() {
    // GIVEN
    long expected = 8L;
    // WHEN
    ThrowingCallable code = () -> assertions.as("test")
                                            .overridingErrorMessage(() -> format("new error message, expected value was : '%s'",
                                                                                 expected))
                                            .isEqualTo(expected);
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage("[test] new error message, expected value was : '8'");
  }

}
