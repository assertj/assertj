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
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

class AbstractAssert_usingRecursiveComparison_Test {

  @Test
  void should_honor_test_description() {
    // GIVEN
    AbstractAssert<?, ?> assertion = assertThat("foo");
    // WHEN
    AssertionError error = expectAssertionError(() -> assertion.as("test description")
                                                               .usingRecursiveComparison()
                                                               .isEqualTo("bar"));
    // THEN
    then(error).hasMessageContaining("[test description]");
  }

  @Test
  void should_honor_representation() {
    // GIVEN
    AbstractAssert<?, ?> assertion = assertThat("foo");
    // WHEN
    RecursiveComparisonAssert<?> recursiveAssertion = assertion.withRepresentation(UNICODE_REPRESENTATION)
                                                               .usingRecursiveComparison();
    // THEN
    then(recursiveAssertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
  }

  @Test
  void should_honor_overridden_error_message() {
    // GIVEN
    AbstractAssert<?, ?> assertion = assertThat("foo");
    String errorMessage = "boom";
    // WHEN
    RecursiveComparisonAssert<?> recursiveAssertion = assertion.overridingErrorMessage(errorMessage)
                                                               .usingRecursiveComparison();
    // THEN
    then(recursiveAssertion.info.overridingErrorMessage()).isEqualTo(errorMessage);
  }

}
