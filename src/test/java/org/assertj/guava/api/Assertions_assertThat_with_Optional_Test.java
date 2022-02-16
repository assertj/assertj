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
package org.assertj.guava.api;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.guava.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.google.common.base.Optional;

/**
 * @author Joel Costigliola
 */
@SuppressWarnings("Guava")
class Assertions_assertThat_with_Optional_Test {

  @Test
  void should_create_Assert() {
    // GIVEN
    Optional<String> actual = Optional.of("value");
    // WHEN
    OptionalAssert<String> assertion = assertThat(actual);
    // THEN
    then(assertion).isNotNull();
  }

  @Test
  void should_pass_actual() {
    // GIVEN
    Optional<String> actual = Optional.of("value");
    // WHEN
    Optional<String> result = assertThat(actual).getActual();
    // THEN
    then(result).isSameAs(actual);
  }

}
