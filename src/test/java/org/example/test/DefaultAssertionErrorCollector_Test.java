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
 * Copyright 2012-2021 the original author or authors.
 */
package org.example.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.assertj.core.api.DefaultAssertionErrorCollector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
                                                         "(DefaultAssertionErrorCollector_Test.java:34)");
    then(decoratedErrors.get(1)).hasMessageContaining("at DefaultAssertionErrorCollector_Test.lambda",
                                                      "(DefaultAssertionErrorCollector_Test.java:35)");
  }

}
