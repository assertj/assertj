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
package org.assertj.core.extention;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.api.junit.jupiter.SoftlyExtension;
import org.assertj.core.error.ShouldNotBeNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SoftlyExtension.class)
@DisplayName("SoftlyExtension hasInstance")
class SoftlyExtensionInstanceTest {

  // private SoftAssertions softlyNotAnnotated;

  private SoftAssertions softly;

  @Test
  void should_pass_if_not_null() {
    // GIVEN/WHEN/THEN
    assertThat(softly).isNotNull();
  }

  @Test
  void should_pass_if_not_null2() {
    // GIVEN/WHEN/THEN
    assertThat(softly).isNotNull();
  }

  @Test
  void should_fail_if_null() {
    // GIVEN/WHEN
    String s = null;
    ThrowingCallable code = () -> assertThat(s).isNotNull();

    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(ShouldNotBeNull.shouldNotBeNull().create());
  }

  @Nested
  @ExtendWith(SoftlyExtension.class)
  @DisplayName("SoftlyExtension Nested Should hasInstance")
  class SoftlyNestedMethodLifecycle {

    private SoftAssertions softlyInner;

    @Test
    void should_pass_if_inner_field_has_instance() {
      // GIVEN/WHEN/THEN
      assertThat(softlyInner).isNotNull();
    }

    @Test
    void should_pass_if_parent_field_has_instance() {
      // GIVEN/WHEN/THEN
      assertThat(softly).isNotNull();
    }
  }
}
