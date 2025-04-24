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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api.abstract_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenIllegalArgumentException;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.testkit.Jedi;
import org.junit.jupiter.api.Test;

class AbstractAssert_satisfies_with_Consumers_Test {
  private final Jedi yoda = new Jedi("Yoda", "Green");

  @Test
  void should_pass_satisfying_single_requirement() {
    // GIVEN
    Consumer<Jedi> isNamedYoda = jedi -> assertThat(jedi.getName()).as("check yoda").isEqualTo("Yoda");
    // WHEN-THEN
    then(yoda).satisfies(isNamedYoda);
  }

  @Test
  void should_pass_satisfying_multiple_requirements() {
    // GIVEN
    Consumer<Jedi> isNamedYoda = jedi -> assertThat(jedi.getName()).as("check yoda").isEqualTo("Yoda");
    Consumer<Jedi> hasGreenLightSaber = jedi -> assertThat(jedi.lightSaberColor).as("check light saber").isEqualTo("Green");
    // WHEN/THEN
    then(yoda).satisfies(hasGreenLightSaber, isNamedYoda);
  }

  @Test
  void should_fail_not_satisfying_single_requirement() {
    // GIVEN
    Consumer<Jedi> isNamedVader = jedi -> assertThat(jedi.getName()).as("check vader").isEqualTo("Darth Vader");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(yoda).satisfies(isNamedVader));
    // THEN
    then(assertionError).hasMessageContaining("check vader");
  }

  @Test
  void should_fail_not_satisfying_any_requirements() {
    // GIVEN
    Consumer<Jedi> isNamedVader = jedi -> assertThat(jedi.getName()).as("check vader").isEqualTo("Darth Vader");
    Consumer<Jedi> isDarth = jedi -> assertThat(jedi.getName()).as("check darth").contains("Darth");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(yoda).satisfies(isNamedVader, isDarth));
    // THEN
    then(assertionError).hasMessageContainingAll("check vader", "check darth");
  }

  @Test
  void should_fail_not_satisfying_some_requirements() {
    // GIVEN
    Consumer<Jedi> isNamedYoda = jedi -> assertThat(jedi.getName()).as("check yoda").isEqualTo("Yoda");
    Consumer<Jedi> isDarth = jedi -> assertThat(jedi.getName()).as("check darth").contains("Darth");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(yoda).satisfies(isNamedYoda, isDarth));
    // THEN
    then(assertionError).hasMessageContaining("check darth")
                        .hasMessageNotContaining("check yoda");
  }

  @Test
  void should_satisfy_supertype_consumer() {
    // GIVEN
    Consumer<Object> notNullObjectConsumer = jedi -> assertThat(jedi).isNotNull();
    // WHEN/THEN
    then(yoda).satisfies(notNullObjectConsumer);
  }

  @Test
  void should_fail_if_consumer_is_null() {
    // GIVEN
    Consumer<Jedi> nullRequirements = null;
    // WHEN/THEN
    thenIllegalArgumentException().isThrownBy(() -> assertThat(yoda).satisfies(nullRequirements))
                                  .withMessage("No assertions group should be null");
  }

  @Test
  void should_fail_if_one_of_the_consumers_is_null() {
    // GIVEN
    Consumer<Jedi> nullRequirement = null;
    Consumer<Jedi> nonNullRequirement = jedi -> assertThat(true).isTrue();
    // WHEN/THEN
    thenIllegalArgumentException().isThrownBy(() -> assertThat(yoda).satisfies(nonNullRequirement, nullRequirement))
                                  .withMessage("No assertions group should be null");
  }

  @Test
  void should_run_consumers_only_once() {
    // GIVEN
    AbstractAssert<?, ?> assertion = assertThat("actualValue");
    AtomicInteger invocationCount = new AtomicInteger();
    Consumer<Object> failingConsumer = s -> {
      invocationCount.incrementAndGet();
      assertThat(s).isEqualTo("anotherValue");
    };
    // WHEN
    expectAssertionError(() -> assertion.satisfies(failingConsumer));
    // THEN
    then(invocationCount).hasValue(1);
  }
}
