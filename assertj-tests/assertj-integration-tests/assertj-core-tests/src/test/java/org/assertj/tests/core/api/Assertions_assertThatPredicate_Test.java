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
package org.assertj.tests.core.api;

import static org.assertj.core.api.Assertions.assertThatPredicate;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.function.Predicate;
import org.assertj.core.api.PredicateAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Assertions_assertThatPredicate_Test {

  private Predicate<String> actual;

  @BeforeEach
  void before() {
    actual = value -> value.equals("something");
  }

  @Test
  void should_create_Assert() {
    // GIVEN
    PredicateAssert<String> assertions = assertThatPredicate(actual);
    // WHEN/THEN
    then(assertions).isNotNull();
  }

}
