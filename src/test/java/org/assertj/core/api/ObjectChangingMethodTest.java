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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS;

import java.util.UUID;

import org.assertj.core.util.introspection.PropertyOrFieldSupport;
import org.junit.jupiter.api.Test;

/**
 * Tests for methods which change the object under assertion, providing a new assertion instance.
 *
 * @author Stefano Cordio
 */
public interface ObjectChangingMethodTest<ASSERT extends AbstractAssert<ASSERT, ?>> {

  ASSERT getAssertion();

  AbstractAssert<?, ?> invoke_object_changing_method(ASSERT assertion);

  @Test
  default void should_honor_registered_comparator() {
    // GIVEN
    ASSERT assertion = getAssertion().usingComparator(ALWAY_EQUALS);
    // WHEN
    AbstractAssert<?, ?> result = invoke_object_changing_method(assertion);
    // THEN
    result.isEqualTo(UUID.randomUUID()); // random value to avoid false positives
  }

  @Test
  default void should_keep_existing_assertion_state() {
    // GIVEN
    ASSERT assertion = getAssertion().as("description")
                                     .withFailMessage("error message")
                                     .withRepresentation(UNICODE_REPRESENTATION)
                                     .usingComparator(ALWAY_EQUALS);
    // WHEN
    AbstractAssert<?, ?> result = invoke_object_changing_method(assertion);
    // THEN
    then(result).hasFieldOrPropertyWithValue("objects", extractObjectField(assertion))
                .extracting(AbstractAssert::getWritableAssertionInfo)
                .isEqualToComparingFieldByField(assertion.info);
  }

  static Object extractObjectField(AbstractAssert<?, ?> assertion) {
    return PropertyOrFieldSupport.EXTRACTION.getValueOf("objects", assertion);
  }

}
