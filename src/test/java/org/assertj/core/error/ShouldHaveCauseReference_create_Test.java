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
import static org.assertj.core.error.ShouldHaveCauseReference.shouldHaveCauseReference;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldHaveCauseReference#shouldHaveCauseReference(Throwable, Throwable)}</code>.
 *
 * @author Mike Gilchrist
 */
public class ShouldHaveCauseReference_create_Test {

  private static final TestDescription DESCRIPTION = new TestDescription("TEST");

  @Test
  public void should_create_error_message_for_expected_without_actual() {
    // GIVEN
    Throwable actualCause = null;
    Throwable expectedCause = new RuntimeException("hello");

    // WHEN
    String actual = shouldHaveCauseReference(actualCause, expectedCause).create(DESCRIPTION);

    // THEN
    assertThat(actual).isEqualTo(format("[TEST] %n"
                                        + "Expecting actual cause reference to be:%n"
                                        + " <\"java.lang.RuntimeException: hello\">%n"
                                        + "but was:%n"
                                        + " <\"null\">."));
  }

  @Test
  public void should_create_error_message_for_expected_with_actual() {
    // GIVEN
    Throwable actualCause = new NullPointerException();
    Throwable expectedCause = new RuntimeException("hello");

    // WHEN
    String actual = shouldHaveCauseReference(actualCause, expectedCause).create(DESCRIPTION);

    // THEN
    assertThat(actual).isEqualTo(format("[TEST] %n"
                                        + "Expecting actual cause reference to be:%n"
                                        + " <\"java.lang.RuntimeException: hello\">%n"
                                        + "but was:%n"
                                        + " <\"java.lang.NullPointerException\">."));
  }
}
