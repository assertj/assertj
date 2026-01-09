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
package org.example.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.extractor.Extractors.byName;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.DefaultAssertionErrorCollector;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

// not in an assertj package to be able to check the stack trace as we filter the stack trace element in assertj packages
class DefaultAssertionErrorCollector_Test {

  private final DefaultAssertionErrorCollector defaultAssertionErrorCollector = new DefaultAssertionErrorCollector();

  @Test
  void decorated_AssertionFailedError_should_keep_actual_and_expected_values_when_populated() {
    // GIVEN
    var error = expectAssertionError(() -> assertThat("foo").isEqualTo("bar"));
    defaultAssertionErrorCollector.collectAssertionError(error);
    // WHEN
    AssertionError decoratedError = defaultAssertionErrorCollector.assertionErrorsCollected().get(0);
    // THEN
    then(decoratedError).isInstanceOf(AssertionFailedError.class);
    Object actualInOriginalError = byName("actual.value").apply(error);
    Object actualInDecoratedError = byName("actual.value").apply(decoratedError);
    then(actualInDecoratedError).isSameAs(actualInOriginalError);
    Object expectedInOriginalError = byName("expected.value").apply(error);
    Object expectedInDecoratedError = byName("expected.value").apply(decoratedError);
    then(expectedInDecoratedError).isSameAs(expectedInOriginalError);
  }

  @Test
  void decorated_AssertionFailedError_should_not_have_null_actual_and_expected_values_when_not_populated() {
    // GIVEN
    AssertionError error = new AssertionFailedError("boom");
    defaultAssertionErrorCollector.collectAssertionError(error);
    // WHEN
    AssertionError decoratedError = defaultAssertionErrorCollector.assertionErrorsCollected().get(0);
    // THEN
    then(decoratedError).isInstanceOf(AssertionFailedError.class)
                        .hasMessage(error.getMessage());
    var decoratedAssertionFailedError = (AssertionFailedError) decoratedError;
    then(decoratedAssertionFailedError.isActualDefined()).isFalse();
    then(decoratedAssertionFailedError.isExpectedDefined()).isFalse();
  }

}
