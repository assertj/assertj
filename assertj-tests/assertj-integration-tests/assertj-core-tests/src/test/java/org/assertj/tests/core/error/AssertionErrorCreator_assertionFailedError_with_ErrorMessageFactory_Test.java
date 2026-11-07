/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeEqualIgnoringCase.shouldBeEqual;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.description.Description;
import org.assertj.core.error.AssertionErrorCreator;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.tests.core.testkit.TestDescription;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

/**
 * Tests for <code>{@link AssertionErrorCreator#assertionError(AssertionInfo, ErrorMessageFactory, Object, Object)}</code>.
 */
class AssertionErrorCreator_assertionFailedError_with_ErrorMessageFactory_Test {

  private final WritableAssertionInfo info = new WritableAssertionInfo();;

  @Test
  void should_create_use_overriding_error_message_if_it_is_specified() {
    // GIVEN
    info.overridingErrorMessage("my message");
    ErrorMessageFactory shouldBeEqual = shouldBeEqual("Yoda", "Luke");
    // WHEN
    var assertionError = AssertionErrorCreator.instance().assertionError(info, shouldBeEqual, "Yoda", "Luke");
    // THEN
    then(assertionError).isInstanceOfSatisfying(AssertionFailedError.class, error -> {
      assertThat(error).hasMessage("my message");
      assertThat(error.getActual().getEphemeralValue()).isEqualTo("Yoda");
      assertThat(error.getExpected().getEphemeralValue()).isEqualTo("Luke");
    });
  }

  @Test
  void should_use_ErrorMessage_when_no_overriding_error_message_is_specified() {
    // GIVEN
    Description description = new TestDescription("description");
    info.description(description);
    ErrorMessageFactory shouldBeEqual = shouldBeEqual("Yoda", "Luke");
    // WHEN
    var assertionError = AssertionErrorCreator.instance().assertionError(info, shouldBeEqual, "Yoda", "Luke");
    // THEN
    then(assertionError).isInstanceOfSatisfying(AssertionFailedError.class, error -> {
      assertThat(error).hasMessage("[description] " + shouldBeEqual.create());
      assertThat(error.getActual().getEphemeralValue()).isEqualTo("Yoda");
      assertThat(error.getExpected().getEphemeralValue()).isEqualTo("Luke");
    });
  }
}
