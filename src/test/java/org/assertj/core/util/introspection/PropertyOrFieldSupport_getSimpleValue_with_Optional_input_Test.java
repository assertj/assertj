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
package org.assertj.core.util.introspection;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class PropertyOrFieldSupport_getSimpleValue_with_Optional_input_Test {

  private final PropertyOrFieldSupport underTest = PropertyOrFieldSupport.EXTRACTION;

  @Test
  void should_return_optional_value_if_optional_is_not_empty() {
    // GIVEN
    Optional<?> input = Optional.of("string");
    // WHEN
    Object value = underTest.getSimpleValue("value", input);
    // THEN
    then(value).isEqualTo("string");
  }

  @Test
  void should_fail_if_optional_is_empty() {
    // GIVEN
    Optional<?> input = Optional.empty();
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.getSimpleValue("value", input));
    // THEN
    then(thrown).isInstanceOf(NoSuchElementException.class);
  }

}
