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
package org.assertj.core.presentation;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.assertj.core.configuration.Configuration;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link StandardRepresentation#toStringOf(Throwable)}</code>.
 *
 *  @author XiaoMingZHM Eveneko
 */
public class StandardRepresentation_throwable_format_Test {
  private static final Representation REPRESENTATION = new StandardRepresentation();

  // Just to make sure the stack trace is long enough.
  static class Test1 {
    static class Test2 {

      static void boom2() {
        throw new RuntimeException();
      }
    }

    static void boom() {
      Test2.boom2();
    }
  }

  @Test
  public void should_not_display_stacktrace_if_maxStackTraceElementsDisplayed_is_zero() {
    // GIVEN
    Configuration configuration = new Configuration();
    configuration.setMaxStackTraceElementsDisplayed(0);
    configuration.apply();
    // WHEN
    String toString = REPRESENTATION.toStringOf(catchThrowable(() -> Test1.boom()));
    // THEN
    assertThat(toString).isEqualTo("java.lang.RuntimeException");
  }

  @Test
  public void should_display_the_configured_number_of_stacktrace_elements() {
    // GIVEN
    Configuration configuration = new Configuration();
    // configuration.setMaxStackTraceElementsDisplayed(3);
    configuration.apply();
    // WHEN
    String toString = REPRESENTATION.toStringOf(catchThrowable(() -> Test1.boom()));
    // THEN
    assertThat(toString).containsSubsequence(format("java.lang.RuntimeException%n"),
                                             format("\tat org.assertj.core.presentation.StandardRepresentation_throwable_format_Test$Test1$Test2.boom2(StandardRepresentation_throwable_format_Test.java:"),
                                             format("\tat org.assertj.core.presentation.StandardRepresentation_throwable_format_Test$Test1.boom(StandardRepresentation_throwable_format_Test.java"),
                                             format("\tat org.assertj.core.presentation.StandardRepresentation_throwable_format_Test.lambda"),
                                             format("\t...(69 remaining lines not displayed - this can be changed with Assertions.setMaxStackTraceElementsDisplayed)"));
  }

  @Test
  public void should_display_the_full_stacktrace() {
    // GIVEN
    Configuration configuration = new Configuration();
    configuration.setMaxStackTraceElementsDisplayed(100);
    configuration.apply();
    // WHEN
    String toString = REPRESENTATION.toStringOf(catchThrowable(() -> Test1.boom()));
    // THEN
    assertThat(toString).startsWith(format("java.lang.RuntimeException%n"
                                           + "\tat org.assertj.core.presentation.StandardRepresentation_throwable_format_Test$Test1$Test2.boom2(StandardRepresentation_throwable_format_Test.java"))
                        .doesNotContain("remaining lines not displayed");
  }

}
