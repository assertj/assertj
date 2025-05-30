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
import static org.assertj.core.error.ShouldHaveCauseInstance.shouldHaveCauseInstance;
import static org.assertj.core.util.Throwables.getStackTrace;

import org.junit.jupiter.api.Test;

class ShouldHaveCauseInstance_create_Test {

  @Test
  void should_create_error_message_for_no_cause() {
    // GIVEN
    Throwable actual = new RuntimeException("boom %s");
    Throwable expected = new IllegalStateException();
    // WHEN
    String message = shouldHaveCauseInstance(actual, expected.getClass()).create();
    // THEN
    then(message).isEqualTo("%nExpecting a throwable with cause being an instance of:%n" +
                            "  %s%n" +
                            "but current throwable has no cause." +
                            "%nThrowable that failed the check:%n%s", expected, getStackTrace(actual));
  }

  @Test
  void should_create_error_message_for_wrong_cause() {
    // GIVEN
    Throwable expected = new IllegalStateException();
    Throwable cause = new IllegalAccessError("oops...% %s %n");
    Throwable actual = new RuntimeException(cause);
    // WHEN
    String message = shouldHaveCauseInstance(actual, expected.getClass()).create();
    // THEN
    then(message).isEqualTo(format("%nExpecting a throwable with cause being an instance of:%n" +
                                   "  java.lang.IllegalStateException%n" +
                                   "but was an instance of:%n" +
                                   "  java.lang.IllegalAccessError%n" +
                                   "Throwable that failed the check:%n" +
                                   "%n" +
                                   "%s", getStackTrace(actual)));
  }
}
