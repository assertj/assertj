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

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveCauseReference.shouldHaveCauseReference;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.Throwables.getStackTrace;

import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldHaveCauseReference#shouldHaveCauseReference(Throwable, Throwable)}</code>.
 *
 * @author Mike Gilchrist
 * @author weiyilei
 */
class ShouldHaveCauseReference_create_Test {

  @Test
  void should_create_error_message_when_expected_throwable_has_no_cause_but_actual_has_one() {
    // GIVEN
    Throwable actualCause = null;
    Throwable expectedCause = new IllegalStateException();
    // WHEN
    String message = shouldHaveCauseReference(actualCause, expectedCause).create();
    // THEN
    then(message).isEqualTo(format("Expecting actual cause reference to be:%n" +
                                   "  %s%n" +
                                   "but was:%n" +
                                   "  null",
                                   STANDARD_REPRESENTATION.toStringOf(expectedCause)));
  }

  @Test
  void should_create_error_message_when_expected_and_actual_cause_are_not_the_same() {
    // GIVEN
    Throwable expectedCause = new IllegalStateException();
    Throwable actual = new RuntimeException(new IllegalAccessError("oops...% %s %n"));
    Throwable actualCause = actual.getCause();
    // WHEN
    String message = shouldHaveCauseReference(actualCause, expectedCause).create();
    // THEN
    then(message).isEqualTo(format("Expecting actual cause reference to be:%n" +
                                   "  %s%n" +
                                   "but was:%n" +
                                   "  %s%n" +
                                   "actual cause that failed the check:%n%n" +
                                   "%s",
                                   STANDARD_REPRESENTATION.toStringOf(expectedCause),
                                   STANDARD_REPRESENTATION.toStringOf(actualCause),
                                   getStackTrace(actualCause)));
  }
}
