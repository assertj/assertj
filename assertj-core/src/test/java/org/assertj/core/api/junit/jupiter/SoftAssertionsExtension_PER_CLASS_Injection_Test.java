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
package org.assertj.core.api.junit.jupiter;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.BDDSoftAssertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@ExtendWith(SoftAssertionsExtension.class)
@DisplayName("SoftAssertionsExtension PER_CLASS injection test")
@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD) // Just in case we move to parallel threads in the future
class SoftAssertionsExtension_PER_CLASS_Injection_Test {

  // use mix of private and package-private here to test behaviour in the variety of different circumstances.
  @InjectSoftAssertions
  SoftAssertions softly;

  @InjectSoftAssertions
  private BDDSoftAssertions deftly;

  SoftAssertions unannotated;

  @Test
  void should_pass_if_not_null() {
    assertThat(softly).isNotNull();
  }

  @Test
  void bdd_should_pass_if_not_null() {
    assertThat(deftly).isNotNull();
    assertThat(deftly.getDelegate().get()).isSameAs(softly.getDelegate().get());
  }

  @Test
  void should_have_same_collector_as_parameter(CustomSoftAssertions custom) {
    assertThat(custom.getDelegate().get()).isSameAs(softly.getDelegate().get());
  }

  @Test
  void should_not_inject_into_unannotated_field() {
    assertThat(unannotated).isNull();
  }

  @Nested
  @ExtendWith(SoftAssertionsExtension.class)
  @DisplayName("nested test class without SoftAssertions field")
  class NestedMethodLifecycle {

    @Test
    void should_use_parent_SoftAssertions_initialized_field() {
      assertThat(softly).isNotNull();
    }
  }

  @Nested
  @ExtendWith(SoftAssertionsExtension.class)
  @DisplayName("nested test class with SoftAssertions field")
  class SoftlyNestedMethodLifecycle {

    @InjectSoftAssertions
    SoftAssertions nestedSoftly;

    @Test
    void should_use_own_SoftAssertions_initialized_field() {
      assertThat(nestedSoftly).isNotNull();
      assertThat(nestedSoftly.getDelegate().get()).isSameAs(softly.getDelegate().get());
    }

  }
}
