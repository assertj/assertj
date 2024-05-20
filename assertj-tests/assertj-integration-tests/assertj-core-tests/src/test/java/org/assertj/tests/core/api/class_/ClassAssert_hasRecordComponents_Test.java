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
package org.assertj.tests.core.api.class_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeRecord.shouldBeRecord;
import static org.assertj.core.error.ShouldHaveRecordComponents.shouldHaveRecordComponents;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.Sets.set;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author Louis Morgan
 */
class ClassAssert_hasRecordComponents_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Class<?> actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasRecordComponents("component"));
    // THEN
    then(error).hasMessage(shouldNotBeNull().create());
  }

  @ParameterizedTest
  @ValueSource(classes = {
      String.class,
      ArrayList.class,
      ValueSource.class
  })
  void should_fail_if_actual_is_not_a_record(Class<?> actual) {
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasRecordComponents("component"));
    // THEN
    then(error).hasMessage(shouldBeRecord(actual).create());
  }

  @Test
  void should_pass_if_record_has_expected_component() {
    // WHEN/THEN
    assertThat(MyRecord.class).hasRecordComponents("componentOne");
  }

  @Test
  void should_pass_if_record_has_expected_components() {
    // WHEN/THEN
    assertThat(MyRecord.class).hasRecordComponents("componentOne", "componentTwo");
  }

  @Test
  void should_fail_if_record_components_are_missing() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(MyRecord.class).hasRecordComponents("componentOne",
                                                                                                              "missing"));
    // THEN
    then(assertionError).hasMessage(shouldHaveRecordComponents(MyRecord.class,
                                                               set("componentOne", "missing"),
                                                               set("missing")).create());
  }

  private record MyRecord(String componentOne, String componentTwo) {
  }

}
