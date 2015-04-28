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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.api.abstract_;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.ConcreteAssert;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests for <code>{@link AbstractAssert#overridingErrorMessage(String, Object...)}</code>.
 * 
 * @author Joel Costigliola
 */
public class AbstractAssert_overridingErrorMessage_Test {

  private ConcreteAssert assertions;

  @Before
  public void setUp() {
    assertions = new ConcreteAssert(6L);
  }

  @Test
  public void should_pass_with_error_message_overridden() {
    assertions.overridingErrorMessage("new error message").isEqualTo(6L);
  }

  @Test
  public void should_fail_with_overridden_error_message() {
    try {
      assertions.overridingErrorMessage("new error message").isEqualTo(8L);
    } catch (AssertionError err) {
      assertThat(err.getMessage()).isEqualTo("new error message");
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_with_overridden_error_message_not_interpreted_with_string_format_feature_as_no_args_are_given() {
    try {
      assertions.overridingErrorMessage("new error message with special character like (%)").isEqualTo(8L);
    } catch (AssertionError err) {
      assertThat(err.getMessage()).isEqualTo("new error message with special character like (%)");
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
  
  @Test
  public void should_fail_with_overridden_error_message_interpreted_with_string_format_feature() {
    try {
      long expected = 8L;
      assertions.overridingErrorMessage("new error message, expected value was : '%s'", expected).isEqualTo(expected);
    } catch (AssertionError err) {
      assertThat(err.getMessage()).isEqualTo("new error message, expected value was : '8'");
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_with_description_and_overridden_error_message_using_string_format_feature() {
    try {
      long expected = 8L;
      assertions.as("test").overridingErrorMessage("new error message, expected value was : '%s'", expected).isEqualTo(expected);
    } catch (AssertionError err) {
      assertThat(err.getMessage()).isEqualTo("[test] new error message, expected value was : '8'");
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_return_this() {
    assertThat(assertions.overridingErrorMessage("")).isSameAs(assertions);
  }
}