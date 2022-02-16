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
package org.assertj.guava.api;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.google.common.base.Optional;

/**
 * @author Kornel
 * @author Joel Costigliola
 */
public class OptionalAssert_contains_Test {

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    Optional<String> actual = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).contains("Test 2"));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(actualIsNull());
  }

  @Test
  public void should_fail_when_option_does_not_contain_expected_value() {
    // GIVEN
    final Optional<String> actual = Optional.of("Test");
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).contains("Test 2"));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(format("%n" +
                                            "Expecting Optional to contain value %n" +
                                            "  \"Test 2\"%n" +
                                            " but contained %n" +
                                            "  \"Test\""));
  }

  @Test
  public void should_fail_when_optional_contains_nothing() {
    // GIVEN
    final Optional<String> actual = Optional.absent();
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).contains("Test"));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage("Expecting Optional to contain \"Test\" but contained nothing (absent Optional)");
  }

  @Test
  public void should_pass_when_actual_contains_expected_value() {
    // GIVEN
    final Optional<String> actual = Optional.of("Test");
    // THEN
    assertThat(actual).contains("Test");
  }

  @Test
  public void should_pass_when_actual_contains_expected_value_for_arrays() {
    // GIVEN
    final Optional<byte[]> actual = Optional.of(new byte[] { 1, 2, 3 });
    // THEN
    assertThat(actual).contains(new byte[] { 1, 2, 3 });
  }
}
