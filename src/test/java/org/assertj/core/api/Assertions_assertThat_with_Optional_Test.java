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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link org.assertj.core.api.Assertions#assertThat(java.util.Optional)}</code>.
 *
 * @author Alex Ruiz
 * @author Mikhail Mazursky
 * @author Alexander Bischof
 */
class Assertions_assertThat_with_Optional_Test {

  private Optional<String> actual;

  @BeforeEach
  void before() {
    actual = Optional.of("String");
  }

  @Test
  void should_create_Assert() {
    OptionalAssert<String> assertions = Assertions.assertThat(actual);
    assertThat(assertions).isNotNull();
  }

  @Test
  void should_pass_actual() {
    OptionalAssert<String> assertions = Assertions.assertThat(actual);
    assertThat(actual).isSameAs(assertions.actual);
  }
}
