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
package org.assertj.core.api.optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.INTEGER;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
import static org.assertj.core.error.OptionalShouldBePresent.shouldBePresent;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.Optional;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.InstanceOfAssertFactory;
import org.assertj.core.api.NavigationMethodBaseTest;
import org.assertj.core.api.OptionalAssert;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link OptionalAssert#get(InstanceOfAssertFactory)}</code>.
 *
 * @author Stefano Cordio
 */
class OptionalAssert_get_with_InstanceOfAssertFactory_Test implements NavigationMethodBaseTest<OptionalAssert<String>> {

  private final Optional<String> optional = Optional.of("Frodo");

  @Test
  void should_fail_if_optional_is_null() {
    // GIVEN
    Optional<String> optional = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(optional).get(STRING));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_optional_is_empty() {
    // GIVEN
    Optional<String> optional = Optional.empty();
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(optional).get(STRING));
    // THEN
    then(assertionError).hasMessage(shouldBePresent(optional).create());
  }

  @Test
  void should_fail_throwing_npe_if_assert_factory_is_null() {
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(optional).get(null));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage(shouldNotBeNull("instanceOfAssertFactory").create());
  }

  @Test
  void should_pass_allowing_type_narrowed_assertions_if_optional_contains_an_instance_of_the_factory_type() {
    // WHEN
    AbstractStringAssert<?> result = assertThat(optional).get(STRING);
    // THEN
    result.startsWith("Frodo");
  }

  @Test
  void should_fail_if_optional_does_not_contain_an_instance_of_the_factory_type() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(optional).get(INTEGER));
    // THEN
    then(assertionError).hasMessageContainingAll("Expecting actual:", "to be an instance of:", "but was instance of:");
  }

  @Override
  public OptionalAssert<String> getAssertion() {
    return assertThat(optional);
  }

  @Override
  public AbstractAssert<?, ?> invoke_navigation_method(OptionalAssert<String> assertion) {
    return assertion.get(STRING);
  }

}
