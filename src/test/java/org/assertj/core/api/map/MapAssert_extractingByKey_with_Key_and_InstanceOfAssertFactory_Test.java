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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.map;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.INTEGER;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS_STRING;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.InstanceOfAssertFactory;
import org.assertj.core.api.MapAssert;
import org.assertj.core.util.introspection.PropertyOrFieldSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link MapAssert#extractingByKey(Object, InstanceOfAssertFactory)}</code>.
 *
 * @author Stefano Cordio
 */
@DisplayName("MapAssert extractingByKey(KEY, InstanceOfAssertFactory)")
class MapAssert_extractingByKey_with_Key_and_InstanceOfAssertFactory_Test {

  private static final Object NAME = "name";
  private Map<Object, Object> map;

  @BeforeEach
  void setup() {
    map = new HashMap<>();
    map.put(NAME, "kawhi");
    map.put("age", 25);
  }

  @Test
  void should_fail_throwing_npe_if_assert_factory_is_null() {
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(map).extractingByKey(NAME, null));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage(shouldNotBeNull("instanceOfAssertFactory").create());;
  }

  @Test
  void should_pass_allowing_type_narrowed_assertions_on_value_extracted_from_given_map_key() {
    // WHEN
    AbstractStringAssert<?> result = assertThat(map).extractingByKey(NAME, as(STRING));
    // THEN
    result.startsWith("kaw");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Map<Object, Object> map = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(map).extractingByKey(NAME, as(STRING)));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_extracted_value_is_not_an_instance_of_the_assert_factory_type() {
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(map).extractingByKey(NAME, as(INTEGER)));
    // THEN
    then(error).hasMessageContainingAll("Expecting:", "to be an instance of:", "but was instance of:");
  }

  @Test
  void should_fail_if_key_is_unknown() {
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(map).extractingByKey("unknown", as(STRING)));
    // THEN
    then(error).hasMessageContaining(actualIsNull());
  }

  @Test
  void should_use_key_name_as_description() {
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(map).extractingByKey(NAME, as(STRING)).isNull());
    // THEN
    then(error).hasMessageContaining("[Extracted: name]");
  }

  @Test
  void should_keep_existing_assertion_state() {
    // GIVEN
    MapAssert<Object, Object> assertion = assertThat(map).as("description")
                                                         .withFailMessage("error message")
                                                         .withRepresentation(UNICODE_REPRESENTATION)
                                                         .usingComparator(ALWAY_EQUALS)
                                                         .usingComparatorForFields(ALWAY_EQUALS_STRING, "foo")
                                                         .usingComparatorForType(ALWAY_EQUALS_STRING, String.class);
    // WHEN
    AbstractStringAssert<?> result = assertion.extractingByKey(NAME, as(STRING));
    // THEN
    then(result).hasFieldOrPropertyWithValue("objects", extractObjectField(assertion))
                .extracting(AbstractAssert::getWritableAssertionInfo)
                .isEqualToComparingFieldByField(assertion.info);
  }

  private static Object extractObjectField(AbstractAssert<?, ?> assertion) {
    return PropertyOrFieldSupport.EXTRACTION.getValueOf("objects", assertion);
  }

}
