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
 * Copyright 2012-2024 the original author or authors.
 */
package org.example.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.extractor.Extractors.byName;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.assertj.core.api.DefaultAssertionErrorCollector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

// not in an assertj package to be able to check the stack trace as we filter the stack trace element in assertj packages
@DisplayName("DefaultAssertionErrorCollector assertionErrorsCollected")
class DefaultAssertionErrorCollector_Test {

  private DefaultAssertionErrorCollector defaultAssertionErrorCollector = new DefaultAssertionErrorCollector();

  @Test
  void collected_errors_should_be_decorate_with_line_numbers() {
    // GIVEN
    AssertionError error1 = expectAssertionError(() -> assertThat("foo").isEqualTo("bar"));
    AssertionError error2 = expectAssertionError(() -> assertThat(1).isNegative());
    defaultAssertionErrorCollector.collectAssertionError(error1);
    defaultAssertionErrorCollector.collectAssertionError(error2);
    // WHEN
    List<AssertionError> decoratedErrors = defaultAssertionErrorCollector.assertionErrorsCollected();
    // THEN
    then(decoratedErrors.get(0)).hasMessageContainingAll("at DefaultAssertionErrorCollector_Test.lambda",
                                                         "(DefaultAssertionErrorCollector_Test.java:36)");
    then(decoratedErrors.get(1)).hasMessageContainingAll("at DefaultAssertionErrorCollector_Test.lambda",
                                                         "(DefaultAssertionErrorCollector_Test.java:37)");
  }

  @Test
  void decorated_AssertionFailedError_should_keep_actual_and_expected_values_when_populated() {
    // GIVEN
    AssertionError error = expectAssertionError(() -> assertThat("foo").isEqualTo("bar"));
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
                        .hasMessageContainingAll(error.getMessage(),
                                                 "(DefaultAssertionErrorCollector_Test.java:69)");
    AssertionFailedError decoratedAssertionFailedError = (AssertionFailedError) decoratedError;
    then(decoratedAssertionFailedError.isActualDefined()).isFalse();
    then(decoratedAssertionFailedError.isExpectedDefined()).isFalse();
  }

}
