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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.assertj.core.api.ConcreteAssert;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.assertj.core.api.ConcreteAssert;

/**
 * Tests for <code>AbstractAssert#failWithActualExpectedAndMessage(Object, Object, String, Object...)</code>.
 *
 * @author Joel Costigliola
 * @author Fr Jeremy Krieg
 */
public class AbstractAssert_failWithActualExpectedAndMessage_Test {

  private ConcreteAssert assertion;

  private Object actual = "Actual";
  private Object expected = "Expected";

  @BeforeEach
  public void setup() {
    assertion = new ConcreteAssert("foo");
  }

  @Test
  public void should_fail_with_simple_message() {
    AssertionFailedError afe = catchThrowableOfType(() -> assertion.failWithActualExpectedAndMessage(actual, expected, "fail"),
                                                    AssertionFailedError.class);
    assertThat(afe).hasMessage("fail");
    assertThat(afe.getActual().getEphemeralValue()).isSameAs(actual);
    assertThat(afe.getExpected().getEphemeralValue()).isSameAs(expected);
  }

  @Test
  public void should_fail_with_message_having_args() {
    AssertionFailedError afe = catchThrowableOfType(() -> assertion.failWithActualExpectedAndMessage(actual, expected,
                                                                                                     "fail %d %s", 5, "times"),
                                                    AssertionFailedError.class);
    assertThat(afe).hasMessage("fail 5 times");
    assertThat(afe.getActual().getEphemeralValue()).isSameAs(actual);
    assertThat(afe.getExpected().getEphemeralValue()).isSameAs(expected);
  }

  @Test
  public void should_keep_description_set_by_user() {
    AssertionFailedError afe = catchThrowableOfType(() -> assertion.as("user description")
                                                                   .failWithActualExpectedAndMessage(actual, expected,
                                                                                                     "fail %d %s", 5, "times"),
                                                    AssertionFailedError.class);
    assertThat(afe).hasMessage("[user description] fail 5 times");
    assertThat(afe.getActual().getEphemeralValue()).isSameAs(actual);
    assertThat(afe.getExpected().getEphemeralValue()).isSameAs(expected);
  }

  @Test
  public void should_keep_specific_error_message_and_description_set_by_user() {
    AssertionFailedError afe = catchThrowableOfType(() -> assertion.as("test context")
                                                                   .overridingErrorMessage("my %d errors %s", 5, "!")
                                                                   .failWithActualExpectedAndMessage(actual, expected, "%d %s", 5,
                                                                                                     "time"),
                                                    AssertionFailedError.class);
    assertThat(afe).hasMessage("[test context] my 5 errors !");
    assertThat(afe.getActual().getEphemeralValue()).isSameAs(actual);
    assertThat(afe.getExpected().getEphemeralValue()).isSameAs(expected);
  }

}
