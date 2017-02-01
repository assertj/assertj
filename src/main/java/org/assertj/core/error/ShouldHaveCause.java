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
package org.assertj.core.error;

import static org.assertj.core.util.Objects.areEqual;
import static org.assertj.core.util.Preconditions.checkArgument;

public class ShouldHaveCause extends BasicErrorMessageFactory {

  public static ErrorMessageFactory shouldHaveCause(Throwable actualCause, Throwable expectedCause) {
    checkArgument(expectedCause != null, "expected cause should not be null");
    // actualCause has no cause
    if (actualCause == null) return new ShouldHaveCause(expectedCause);
    // same message => different type
    if (areEqual(actualCause.getMessage(), expectedCause.getMessage()))
      return new ShouldHaveCause(actualCause, expectedCause.getClass());
    // same type => different message
    if (areEqual(actualCause.getClass(), expectedCause.getClass()))
      return new ShouldHaveCause(actualCause, expectedCause.getMessage());
    return new ShouldHaveCause(actualCause, expectedCause);
  }

  private ShouldHaveCause(Throwable actualCause, Throwable expectedCause) {
    super("%n" +
          "Expecting a cause with type:%n" +
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

  private ShouldHaveCause(Throwable expectedCause) {
    super("%n" +
          "Expecting a cause with type:%n" +
          "  <%s>%n" +
          "and message:%n" +
          "  <%s>%n" +
          "but actualCause had no cause.",
          expectedCause.getClass().getName(), expectedCause.getMessage());
  }

  private ShouldHaveCause(Throwable actualCause, Class<? extends Throwable> expectedCauseClass) {
    super("%n" +
          "Expecting a cause with type:%n" +
          "  <%s>%n" +
          "but type was:%n" +
          "  <%s>.",
          expectedCauseClass.getName(), actualCause.getClass().getName());
  }

  private ShouldHaveCause(Throwable actualCause, String expectedCauseMessage) {
    super("%n" +
          "Expecting a cause with message:%n" +
          "  <%s>%n" +
          "but message was:%n" +
          "  <%s>.",
          expectedCauseMessage, actualCause.getMessage());
  }
}
