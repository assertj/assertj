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
package org.assertj.core.tests.java17;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeRecord.shouldNotBeRecord;
import static org.assertj.core.error.ShouldBeSealed.shouldBeSealed;
import static org.assertj.core.error.ShouldBeSealed.shouldNotBeSealed;
import static org.assertj.core.error.ShouldHaveNoPermittedSubclasses.shouldHaveNoPermittedSubclasses;
import static org.assertj.core.error.ShouldHavePermittedSubclasses.shouldHavePermittedSubclasses;
import static org.assertj.core.error.ShouldHaveRecordComponents.shouldHaveRecordComponents;
import static org.assertj.core.util.Sets.set;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author Louis Morgan
 */
class Assertions_assertThat_with_Class_Test {

  @Nested
  class with_record {

    @Test
    void hasRecordComponents_should_pass_if_record_has_expected_component() {
      // WHEN/THEN
      assertThat(MyRecord.class).hasRecordComponents("componentOne");
    }

    @Test
    void hasRecordComponents_should_pass_if_record_has_expected_components() {
      // WHEN/THEN
      assertThat(MyRecord.class).hasRecordComponents("componentOne", "componentTwo");
    }

    @Test
    void hasRecordComponents_should_fail_if_record_components_are_missing() {
      // WHEN
      Throwable thrown = catchThrowable(() -> assertThat(MyRecord.class).hasRecordComponents("componentOne",
                                                                                             "missing"));
      // THEN
      then(thrown).isInstanceOf(AssertionError.class)
                  .hasMessage(shouldHaveRecordComponents(MyRecord.class,
                                                         set("componentOne", "missing"),
                                                         set("missing")).create());
    }

    @Test
    void isNotRecord_should_fail_if_actual_is_a_record() {
      // WHEN
      Throwable thrown = catchThrowable(() -> assertThat(MyRecord.class).isNotRecord());
      // THEN
      then(thrown).isInstanceOf(AssertionError.class)
                  .hasMessage(shouldNotBeRecord(MyRecord.class).create());
    }

    @Test
    void isRecord_should_pass_if_actual_is_a_record() {
      // WHEN/THEN
      assertThat(MyRecord.class).isRecord();
    }

    private record MyRecord(String componentOne, String componentTwo) {
    }

  }

  @Nested
  class with_sealed_class {

    @Test
    void isNotSealed_should_fail_if_actual_is_sealed() {
      // WHEN
      Throwable thrown = catchThrowable(() -> assertThat(SealedClass.class).isNotSealed());
      // THEN
      then(thrown).isInstanceOf(AssertionError.class)
                  .hasMessage(shouldNotBeSealed(SealedClass.class).create());
    }

    @Test
    void isSealed_should_fail_if_actual_is_not_sealed() {
      // WHEN
      Throwable thrown = catchThrowable(() -> assertThat(NonSealedClass.class).isSealed());
      // THEN
      then(thrown).isInstanceOf(AssertionError.class)
                  .hasMessage(shouldBeSealed(NonSealedClass.class).create());
    }

    @Test
    void isSealed_should_pass_if_actual_is_sealed() {
      // WHEN/THEN
      assertThat(SealedClass.class).isSealed();
    }

    @Test
    void hasPermittedSubclasses_should_pass_if_actual_has_permitted_subclass() {
      // WHEN/THEN
      assertThat(SealedClass.class).hasPermittedSubclasses(NonSealedClass.class);
    }

    @Test
    void hasPermittedSubclasses_should_fail_if_actual_does_not_have_one_of_given_classes() {
      // WHEN
      Throwable thrown = catchThrowable(() -> assertThat(SealedClass.class).hasPermittedSubclasses(NonSealedClass.class,
                                                                                                   String.class));
      // THEN
      then(thrown).isInstanceOf(AssertionError.class)
                  .hasMessage(shouldHavePermittedSubclasses(
                                                            SealedClass.class,
                                                            List.of(NonSealedClass.class, String.class),
                                                            List.of(String.class)).create());
    }

    @Test
    void hasNoPermittedSubclasses_should_pass_if_actual_has_no_permitted_subclasses() {
      // WHEN/THEN
      assertThat(NonSealedClass.class).hasNoPermittedSubclasses();
    }

    @Test
    void hasNoPermittedSubclasses_should_fail_if_actual_has_a_permitted_subclass() {
      // WHEN
      Throwable thrown = catchThrowable(() -> assertThat(SealedClass.class).hasNoPermittedSubclasses());
      // THEN
      then(thrown).isInstanceOf(AssertionError.class)
                  .hasMessage(shouldHaveNoPermittedSubclasses(SealedClass.class, List.of(NonSealedClass.class)).create());
    }

    private sealed class SealedClass permits NonSealedClass {
    }

    private non-sealed class NonSealedClass extends SealedClass {
    }

  }

}
