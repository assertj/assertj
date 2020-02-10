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
package org.assertj.core.error;

import static org.assertj.core.util.Preconditions.checkArgument;
import static org.assertj.core.util.Strings.escapePercent;
import static org.assertj.core.util.Throwables.getStackTrace;

import java.util.Objects;

public class ShouldHaveRootCause extends BasicErrorMessageFactory {

  public static ErrorMessageFactory shouldHaveRootCauseWithMessage(Throwable actual, Throwable actualCause,
                                                                   String expectedMessage) {
    checkArgument(actual != null, "actual should not be null");
    checkArgument(expectedMessage != null, "expected root cause message should not be null");
    if (actualCause == null) return new ShouldHaveRootCause(actual, expectedMessage);
    return new ShouldHaveRootCause(actual, actualCause, expectedMessage);
  }

  public static ErrorMessageFactory shouldHaveRootCause(Throwable actual, Throwable actualCause, Throwable expectedCause) {
    checkArgument(actual != null, "actual should not be null");
    checkArgument(expectedCause != null, "expected cause should not be null");
    // actualCause has no cause
    if (actualCause == null) return new ShouldHaveRootCause(actual, expectedCause);
    // same message => different type
    if (Objects.equals(actualCause.getMessage(), expectedCause.getMessage()))
      return new ShouldHaveRootCause(actual, actualCause, expectedCause.getClass());
    // same type => different message
    if (Objects.equals(actualCause.getClass(), expectedCause.getClass()))
      return new ShouldHaveRootCause(actual, actualCause, expectedCause.getMessage());
    return new ShouldHaveRootCause(actual, actualCause, expectedCause);
  }

  public static ErrorMessageFactory shouldHaveRootCause(Throwable actualCause) {
    return new BasicErrorMessageFactory("expecting %s to have a root cause but it did not", actualCause);
  }

  private ShouldHaveRootCause(Throwable actual, Throwable actualCause, Throwable expectedCause) {
    super("%n" +
          "Expecting a root cause with type:%n" +
          "  <%s>%n" +
          "and message:%n" +
          "  <%s>%n" +
          "but type was:%n" +
          "  <%s>%n" +
          "and message was:%n" +
          "  <%s>." +
          "%n" +
          "Throwable that failed the check:%n" +
          "%n" + escapePercent(getStackTrace(actual)), // to avoid AssertJ default String formatting
          expectedCause.getClass().getName(), expectedCause.getMessage(),
          actualCause.getClass().getName(), actualCause.getMessage());
  }

  private ShouldHaveRootCause(Throwable actual, Throwable expectedCause) {
    super("%n" +
          "Expecting a root cause with type:%n" +
          "  <%s>%n" +
          "and message:%n" +
          "  <%s>%n" +
          "but actual had no root cause." +
          "%n" +
          "Throwable that failed the check:%n" +
          "%n" + escapePercent(getStackTrace(actual)), // to avoid AssertJ default String formatting
          expectedCause.getClass().getName(), expectedCause.getMessage());
  }

  private ShouldHaveRootCause(Throwable actual, Throwable actualCause, Class<? extends Throwable> expectedCauseClass) {
    super("%n" +
          "Expecting a root cause with type:%n" +
          "  <%s>%n" +
          "but type was:%n" +
          "  <%s>." +
          "%n" +
          "Throwable that failed the check:%n" +
          "%n" + escapePercent(getStackTrace(actual)), // to avoid AssertJ default String formatting
          expectedCauseClass.getName(), actualCause.getClass().getName());
  }

  private ShouldHaveRootCause(Throwable actual, String expectedMessage) {
    super("%n" +
          "Expecting a root cause with message:%n" +
          "  <%s>%n" +
          "but actual had no root cause." +
          "%n" +
          "Throwable that failed the check:%n" +
          "%n" + escapePercent(getStackTrace(actual)), // to avoid AssertJ default String formatting
          expectedMessage);
  }

  private ShouldHaveRootCause(Throwable actual, Throwable actualCause, String expectedCauseMessage) {
    super("%n" +
          "Expecting a root cause with message:%n" +
          "  <%s>%n" +
          "but message was:%n" +
          "  <%s>." +
          "%n" +
          "Throwable that failed the check:%n" +
          "%n" + escapePercent(getStackTrace(actual)), // to avoid AssertJ default String formatting
          expectedCauseMessage, actualCause.getMessage());
  }
}
