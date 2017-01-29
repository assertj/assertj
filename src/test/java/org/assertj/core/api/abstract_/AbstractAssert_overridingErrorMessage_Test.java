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
package org.assertj.core.api.abstract_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.ExpectedException.none;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.ConcreteAssert;
import org.assertj.core.test.ExpectedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


/**
 * Tests for <code>{@link AbstractAssert#overridingErrorMessage(String, Object...)}</code>.
 * 
 * @author Joel Costigliola
 */
public class AbstractAssert_overridingErrorMessage_Test {

  @Rule
  public ExpectedException thrown = none();

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
    thrown.expectAssertionError("new error message");
    assertions.overridingErrorMessage("new error message").isEqualTo(8L);
  }

  @Test
  public void should_fail_with_overridden_error_message_not_interpreted_with_string_format_feature_as_no_args_are_given() {
    // % has to be escaped as %% because expectAssertionError used String.format on the message
    thrown.expectAssertionError("new error message with special character like (%%)");
    assertions.overridingErrorMessage("new error message with special character like (%)").isEqualTo(8L);
  }
  
  @Test
  public void should_fail_with_overridden_error_message_interpreted_with_string_format_feature() {
    thrown.expectAssertionError("new error message, expected value was : '8'");
    long expected = 8L;
    assertions.overridingErrorMessage("new error message, expected value was : '%s'", expected).isEqualTo(expected);
  }

  @Test
  public void should_fail_with_description_and_overridden_error_message_using_string_format_feature() {
    thrown.expectAssertionError("[test] new error message, expected value was : '8'");
    long expected = 8L;
    assertions.as("test").overridingErrorMessage("new error message, expected value was : '%s'", expected).isEqualTo(expected);
  }

  @Test
  public void should_return_this() {
    assertThat(assertions.overridingErrorMessage("")).isSameAs(assertions);
  }
}