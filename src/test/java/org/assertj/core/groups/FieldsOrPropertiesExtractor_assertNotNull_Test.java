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
package org.assertj.core.groups;

import static org.assertj.core.groups.FieldsOrPropertiesExtractor.extract;
import static org.assertj.core.test.ExpectedException.none;

import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

public class FieldsOrPropertiesExtractor_assertNotNull_Test {
  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_throw_assertion_error_in_absence_of_iterable() {
    thrown.expectAssertionErrorWithMessageContaining("Expecting actual not to be null");
    extract((Iterable<?>) null, null);
  }

  @Test
  public void should_throw_assertion_error_in_absence_of_array() {
    thrown.expectAssertionErrorWithMessageContaining("Expecting actual not to be null");
    extract((Object[]) null, null);
  }
}
