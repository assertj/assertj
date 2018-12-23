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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.util.Objects.areEqual;
import static org.assertj.core.util.Preconditions.checkArgument;

public class ShouldHaveRootCause extends BasicErrorMessageFactory {

  public static ErrorMessageFactory shouldHaveRootCause(Throwable actualCause, Throwable expectedCause) {
    checkArgument(expectedCause != null, "expected cause should not be null");
    // actualCause has no cause
    if (actualCause == null) return new ShouldHaveRootCause(expectedCause);
    // same message => different type
    if (areEqual(actualCause.getMessage(), expectedCause.getMessage()))
      return new ShouldHaveRootCause(actualCause, expectedCause.getClass());
    // same type => different message
    if (areEqual(actualCause.getClass(), expectedCause.getClass()))
      return new ShouldHaveRootCause(actualCause, expectedCause.getMessage());
    return new ShouldHaveRootCause(actualCause, expectedCause);
  }

  private ShouldHaveRootCause(Throwable actualCause, Throwable expectedCause) {
    super("%n" +
          "Expecting a root cause with type:%n" +
          "  <%s>%n" +
          "and message:%n" +
          "  <%s>%n" +
          "but type was:%n" +
          "  <%s>%n" +
          "and message was:%n" +
          "  <%s>.",
          expectedCause.getClass().getName(), expectedCause.getMessage(),
          actualCause.getClass().getName(), actualCause.getMessage());
  }

  private ShouldHaveRootCause(Throwable expectedCause) {
    super("%n" +
          "Expecting a root cause with type:%n" +
          "  <%s>%n" +
          "and message:%n" +
          "  <%s>%n" +
          "but actual had no root cause.",
          expectedCause.getClass().getName(), expectedCause.getMessage());
  }

  private ShouldHaveRootCause(Throwable actualCause, Class<? extends Throwable> expectedCauseClass) {
    super("%n" +
          "Expecting a root cause with type:%n" +
          "  <%s>%n" +
          "but type was:%n" +
          "  <%s>.",
          expectedCauseClass.getName(), actualCause.getClass().getName());
  }

  private ShouldHaveRootCause(Throwable actualCause, String expectedCauseMessage) {
    super("%n" +
          "Expecting a root cause with message:%n" +
          "  <%s>%n" +
          "but message was:%n" +
          "  <%s>.",
          expectedCauseMessage, actualCause.getMessage());
  }
}
