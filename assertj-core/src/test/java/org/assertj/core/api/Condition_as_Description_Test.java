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
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.description.EmptyTextDescription.emptyDescription;
import static org.assertj.core.testkit.TestData.someTextDescription;

import org.assertj.core.description.Description;
import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Condition#as(Description)}</code>.
 *
 * @author Yvonne Wang
 */
class Condition_as_Description_Test {

  private static Description description;

  @BeforeAll
  static void setUpOnce() {
    description = new TestDescription(someTextDescription());
  }

  private Condition<Object> condition;

  @BeforeEach
  void setUp() {
    condition = new TestCondition<>();
  }

  @Test
  void should_set_description() {
    condition.as(description);
    assertThat(condition.description()).isSameAs(description);
  }

  @Test
  void should_replace_null_description_by_an_empty_one() {
    // GIVEN
    TestDescription description = null;
    // WHEN
    condition.as(description);
    // THEN
    then(condition.description()).isEqualTo(emptyDescription());
  }

  @Test
  void should_return_same_condition() {
    Condition<Object> returnedCondition = condition.as(description);
    assertThat(returnedCondition).isSameAs(condition);
  }
}
