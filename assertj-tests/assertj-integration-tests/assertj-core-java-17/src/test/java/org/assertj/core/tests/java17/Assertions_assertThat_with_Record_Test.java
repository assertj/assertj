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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.tests.java17;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeRecord.shouldNotBeRecord;
import static org.assertj.core.error.ShouldHaveRecordComponents.shouldHaveNoRecordComponents;
import static org.assertj.core.error.ShouldHaveRecordComponents.shouldHaveRecordComponents;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * @author Louis Morgan
 */
class Assertions_assertThat_with_Record_Test {

  @Test
  void is_record_should_pass_if_actual_is_a_record() {
    // WHEN/THEN
    assertThat(MyRecord.class).isRecord();
  }

  @Test
  void is_not_record_should_fail_if_actual_is_a_record() {
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(MyRecord.class).isNotRecord());
    // THEN
    then(thrown).isInstanceOf(AssertionError.class)
                .hasMessage(shouldNotBeRecord(MyRecord.class).create());
  }

  @Test
  void has_record_components_should_pass_if_class_has_record_component() {
    // WHEN/THEN
    assertThat(MyRecord.class).hasRecordComponents("recordComponentOne");
  }

  @Test
  void has_record_components_should_pass_if_class_has_record_components() {
    // WHEN/THEN
    assertThat(MyRecord.class).hasRecordComponents("recordComponentOne", "recordComponentTwo");
  }

  @Test
  void has_record_components_should_pass_if_class_has_no_record_components_and_none_are_expected() {
    // WHEN/THEN
    assertThat(EmptyRecord.class).hasRecordComponents();
  }

  @Test
  void has_record_components_should_fail_if_record_components_are_missing() {
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(MyRecord.class).hasRecordComponents("recordComponentOne", "missing"));
    // THEN
    then(thrown).isInstanceOf(AssertionError.class)
      .hasMessage(shouldHaveRecordComponents(MyRecord.class, newLinkedHashSet("recordComponentOne", "missing"), Set.of("missing")).create());
  }

  @Test
  void has_record_components_should_fail_if_no_record_components_are_expected_but_class_has_some() {
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(MyRecord.class).hasRecordComponents());
    // THEN
    then(thrown).isInstanceOf(AssertionError.class)
      .hasMessage(shouldHaveNoRecordComponents(MyRecord.class, newLinkedHashSet("recordComponentOne", "recordComponentTwo")).create());
  }

  record MyRecord(String recordComponentOne, String recordComponentTwo) {
  }

  record EmptyRecord() {
  }

}
