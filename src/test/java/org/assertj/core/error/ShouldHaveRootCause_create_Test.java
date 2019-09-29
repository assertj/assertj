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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.error.ShouldHaveRootCause.shouldHaveRootCause;
import static org.assertj.core.error.ShouldHaveRootCause.shouldHaveRootCauseWithMessage;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link ShouldHaveRootCause#shouldHaveRootCause(Throwable, Throwable)}</code>
 *
 * @author Jack Gough
 */
@DisplayName("ShouldHaveRootCause create")
public class ShouldHaveRootCause_create_Test {

  private static final TestDescription DESCRIPTION = new TestDescription("TEST");

  @Test
  public void should_fail_if_expected_message_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> shouldHaveRootCauseWithMessage(null, null))
                                        .withMessage("expected root cause message should not be null");
  }

  @Test
  public void should_create_error_message_for_expected_without_actual() {
    // GIVEN
    Throwable actualCause = null;
    Throwable expectedCause = new RuntimeException("hello");
    // WHEN
    String actual = shouldHaveRootCause(actualCause, expectedCause).create(DESCRIPTION);
    // THEN
    assertThat(actual).isEqualTo(format("[TEST] %n"
                                        + "Expecting a root cause with type:%n"
                                        + "  <\"java.lang.RuntimeException\">%n"
                                        + "and message:%n"
                                        + "  <\"hello\">%n"
                                        + "but actual had no root cause."));
  }

  @Test
  public void should_create_error_message_for_unequal_types() {
    // GIVEN
    Throwable actualCause = new IllegalArgumentException("one");
    Throwable expectedCause = new RuntimeException("one");
    // WHEN
    String actual = shouldHaveRootCause(actualCause, expectedCause).create(DESCRIPTION);
    // THEN
    assertThat(actual).isEqualTo(format("[TEST] %n"
                                        + "Expecting a root cause with type:%n"
                                        + "  <\"java.lang.RuntimeException\">%n"
                                        + "but type was:%n"
                                        + "  <\"java.lang.IllegalArgumentException\">."));
  }

  @Test
  public void should_create_error_message_for_unequal_messages() {
    // GIVEN
    Throwable actualCause = new RuntimeException("wibble");
    Throwable expectedCause = new RuntimeException("wobble");
    // WHEN
    String actual = shouldHaveRootCause(actualCause, expectedCause).create(DESCRIPTION);
    // THEN
    assertThat(actual).isEqualTo(format("[TEST] %n"
                                        + "Expecting a root cause with message:%n"
                                        + "  <\"wobble\">%n"
                                        + "but message was:%n"
                                        + "  <\"wibble\">."));
  }

  @Test
  public void should_create_error_message_for_unequal_types_and_messages() {
    // GIVEN
    Throwable actualCause = new RuntimeException("wibble");
    Throwable expectedCause = new IllegalArgumentException("wobble");
    // WHEN
    String actual = shouldHaveRootCause(actualCause, expectedCause).create(DESCRIPTION);
    // THEN
    assertThat(actual).isEqualTo(format("[TEST] %n"
                                        + "Expecting a root cause with type:%n"
                                        + "  <\"java.lang.IllegalArgumentException\">%n"
                                        + "and message:%n"
                                        + "  <\"wobble\">%n"
                                        + "but type was:%n"
                                        + "  <\"java.lang.RuntimeException\">%n"
                                        + "and message was:%n"
                                        + "  <\"wibble\">."));
  }

  @Test
  public void should_create_error_message_for_null_root_cause() {
    // GIVEN
    String expectedMessage = "wobble";
    // WHEN
    String actual = shouldHaveRootCauseWithMessage(null, expectedMessage).create(DESCRIPTION);

    // THEN
    assertThat(actual).isEqualTo(format("[TEST] %n" +
                                        "Expecting a root cause with message:%n" +
                                        "  <\"wobble\">%n" +
                                        "but actual had no root cause."));
  }

  @Test
  public void should_create_error_message_for_actual_message_unequal_to_expected() {
    // GIVEN
    Throwable actualCause = new RuntimeException("wibble");
    String expectedMessage = "wobble";
    // WHEN
    String actual = shouldHaveRootCauseWithMessage(actualCause, expectedMessage).create(DESCRIPTION);
    // THEN
    assertThat(actual).isEqualTo(format("[TEST] %n" +
                                        "Expecting a root cause with message:%n" +
                                        "  <\"wobble\">%n" +
                                        "but message was:%n" +
                                        "  <\"wibble\">."));
  }
}
