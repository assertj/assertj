/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.presentation;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.assertj.core.configuration.Configuration;
import org.assertj.core.presentation.Representation;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.tests.core.testkit.MutatesGlobalConfiguration;
import org.junit.jupiter.api.Test;

@MutatesGlobalConfiguration
class StandardRepresentation_throwable_format_Test {

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
  void should_not_display_stacktrace_if_maxStackTraceElementsDisplayed_is_zero() {
    // GIVEN
    Configuration configuration = new Configuration();
    configuration.setMaxStackTraceElementsDisplayed(0);
    configuration.apply();
    // WHEN
    String toString = REPRESENTATION.toStringOf(catchThrowable(Test1::boom));
    // THEN
    then(toString).isEqualTo("java.lang.RuntimeException");
  }

  @Test
  void should_display_the_configured_number_of_stacktrace_elements() {
    // GIVEN
    Configuration configuration = new Configuration();
    // configuration.setMaxStackTraceElementsDisplayed(3);
    configuration.apply();
    // WHEN
    String toString = REPRESENTATION.toStringOf(catchThrowable(Test1::boom));
    // THEN
    then(toString).matches("java\\.lang\\.RuntimeException\\R" +
                           "\\tat org\\.assertj\\.tests\\.core\\.presentation\\.StandardRepresentation_throwable_format_Test\\$Test1\\$Test2\\.boom2\\(StandardRepresentation_throwable_format_Test\\.java:\\d+\\)\\R"
                           +
                           "\\tat org\\.assertj\\.tests\\.core\\.presentation\\.StandardRepresentation_throwable_format_Test\\$Test1\\.boom\\(StandardRepresentation_throwable_format_Test\\.java:\\d+\\)\\R"
                           +
                           "\\tat org\\.assertj\\.core\\.api\\.ThrowableAssert\\.catchThrowable\\(ThrowableAssert\\.java:\\d+\\)\\R"
                           +
                           "\\t\\.{3}\\(\\d+ remaining lines not displayed - this can be changed with Assertions\\.setMaxStackTraceElementsDisplayed\\)");
  }

  @Test
  void should_display_the_full_stacktrace() {
    // GIVEN
    Configuration configuration = new Configuration();
    configuration.setMaxStackTraceElementsDisplayed(100);
    configuration.apply();
    // WHEN
    String toString = REPRESENTATION.toStringOf(catchThrowable(Test1::boom));
    // THEN
    then(toString).startsWith(format("java.lang.RuntimeException%n"
                                     + "\tat org.assertj.tests.core.presentation.StandardRepresentation_throwable_format_Test$Test1$Test2.boom2(StandardRepresentation_throwable_format_Test.java"))
                  .doesNotContain("remaining lines not displayed");
  }

  @Test
  void should_display_toString_when_null_stack() {
    // GIVEN
    Throwable throwable = mock(Throwable.class);
    when(throwable.toString()).thenReturn("throwable string");
    // WHEN
    String actual = REPRESENTATION.toStringOf(throwable);
    // THEN
    then(actual).isEqualTo("throwable string");
  }
}
