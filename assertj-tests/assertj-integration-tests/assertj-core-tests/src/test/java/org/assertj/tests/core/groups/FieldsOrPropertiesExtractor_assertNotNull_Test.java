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
package org.assertj.tests.core.groups;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.groups.FieldsOrPropertiesExtractor.extract;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

class FieldsOrPropertiesExtractor_assertNotNull_Test {

  @Test
  void should_throw_assertion_error_in_absence_of_iterable() {
    // WHEN
    var assertionError = expectAssertionError(() -> extract((Iterable<?>) null, null));
    // THEN
    then(assertionError).hasMessage("Expecting actual not to be null");
  }

  @Test
  void should_throw_assertion_error_in_absence_of_array() {
    // WHEN
    var assertionError = expectAssertionError(() -> extract((Object[]) null, null));
    // THEN
    then(assertionError).hasMessage("Expecting actual not to be null");
  }
}
