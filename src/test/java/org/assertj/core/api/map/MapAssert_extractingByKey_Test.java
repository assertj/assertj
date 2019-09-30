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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS_STRING;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.MapAssert;
import org.assertj.core.util.introspection.PropertyOrFieldSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link MapAssert#extractingByKey(Object)}</code>.
 *
 * @author Stefano Cordio
 */
@DisplayName("MapAssert extractingByKey(K)")
class MapAssert_extractingByKey_Test {

  private static final Object NAME = "name";
  private Map<Object, Object> map;

  @BeforeEach
  void setup() {
    map = new HashMap<>();
    map.put(NAME, "kawhi");
    map.put("age", 25);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Map<Object, Object> map = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(map).extractingByKey(NAME));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_allow_object_assertions_on_value_extracted_from_given_map_key() {
    // WHEN
    AbstractObjectAssert<?, Object> result = assertThat(map).extractingByKey(NAME);
    // THEN
    result.isEqualTo("kawhi");
  }

  @Test
  void should_extract_null_object_from_unknown_key() {
    // WHEN
    AbstractObjectAssert<?, Object> result = assertThat(map).extractingByKey("unknown");
    // THEN
    result.isNull();
  }

  @Test
  void should_use_key_name_as_description() {
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(map).extractingByKey(NAME).isNull());
    // THEN
    then(error).hasMessageContaining("[Extracted: name]");
  }

  @Test
  void should_keep_existing_description_if_set_when_extracting_value_object() {
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(map).as("check name")
                                                                     .extractingByKey(NAME).isNull());
    // THEN
    then(error).hasMessageContaining("[check name]");
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
    AbstractObjectAssert<?, Object> result = assertion.extractingByKey(NAME);
    // THEN
    then(result).hasFieldOrPropertyWithValue("objects", extractObjectField(assertion))
                .extracting(AbstractAssert::getWritableAssertionInfo)
                .isEqualToComparingFieldByField(assertion.info);
  }

  private static Object extractObjectField(AbstractAssert<?, ?> assertion) {
    return PropertyOrFieldSupport.EXTRACTION.getValueOf("objects", assertion);
  }

}
