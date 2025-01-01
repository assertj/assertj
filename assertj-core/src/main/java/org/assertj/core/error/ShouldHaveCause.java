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
package org.assertj.core.error;

import static org.assertj.core.util.Preconditions.checkArgument;
import static org.assertj.core.util.Strings.escapePercent;
import static org.assertj.core.util.Throwables.getStackTrace;

import java.util.Objects;

public class ShouldHaveCause extends BasicErrorMessageFactory {

  public static ErrorMessageFactory shouldHaveCause(Throwable actual, Throwable expectedCause) {
    checkArgument(expectedCause != null, "expected cause should not be null");
    // actual has no cause
    if (actual.getCause() == null) return new ShouldHaveCause(expectedCause);
    // same message => different type
    if (Objects.equals(actual.getCause().getMessage(), expectedCause.getMessage()))
      return new ShouldHaveCause(actual, expectedCause.getClass());
    // same type => different message
    if (Objects.equals(actual.getCause().getClass(), expectedCause.getClass()))
      return new ShouldHaveCause(actual, expectedCause.getMessage());
    return new ShouldHaveCause(actual, expectedCause);
  }

  public static ErrorMessageFactory shouldHaveCause(Throwable actualCause) {
    return new BasicErrorMessageFactory("Expecting actual throwable to have a cause but it did not, actual was:%n%s",
                                        actualCause);
  }

  private ShouldHaveCause(Throwable actual, Throwable expectedCause) {
    super("%n" +
          "Expecting a cause with type:%n" +
          "  %s%n" +
          "and message:%n" +
          "  %s%n" +
          "but type was:%n" +
          "  %s%n" +
          "and message was:%n" +
          "  %s.%n%n" +
          "Throwable that failed the check:%n" +
          escapePercent(getStackTrace(actual)),
          expectedCause.getClass().getName(), expectedCause.getMessage(),
          actual.getCause().getClass().getName(), actual.getCause().getMessage());
  }

  private ShouldHaveCause(Throwable expectedCause) {
    super("%n" +
          "Expecting a cause with type:%n" +
          "  %s%n" +
          "and message:%n" +
          "  %s%n" +
          "but actualCause had no cause.",
          expectedCause.getClass().getName(), expectedCause.getMessage());
  }

  private ShouldHaveCause(Throwable actual, Class<? extends Throwable> expectedCauseClass) {
    super("%n" +
          "Expecting a cause with type:%n" +
          "  %s%n" +
          "but type was:%n" +
          "  %s.%n%n" +
          "Throwable that failed the check:%n" +
          escapePercent(getStackTrace(actual)),
          expectedCauseClass.getName(), actual.getCause().getClass().getName());
  }

  private ShouldHaveCause(Throwable actual, String expectedCauseMessage) {
    super("%n" +
          "Expecting a cause with message:%n" +
          "  %s%n" +
          "but message was:%n" +
          "  %s.%n%n" +
          "Throwable that failed the check:%n" +
          escapePercent(getStackTrace(actual)),
          expectedCauseMessage, actual.getCause().getMessage());
  }
}
