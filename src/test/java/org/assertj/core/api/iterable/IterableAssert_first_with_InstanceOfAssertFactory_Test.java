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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.iterable;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.INTEGER;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsEmpty;

import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.InstanceOfAssertFactory;
import org.assertj.core.api.IterableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link IterableAssert#first(InstanceOfAssertFactory)}</code>.
 * 
 * @author Stefano Cordio
 */
@DisplayName("IterableAssert first(InstanceOfAssertFactory)")
class IterableAssert_first_with_InstanceOfAssertFactory_Test {

  private final Iterable<Object> iterable = asList("string", 42, 0.0);

  @Test
  void should_fail_if_iterable_is_empty() {
    // GIVEN
    Iterable<String> iterable = emptyList();
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(iterable).first(STRING));
    // THEN
    then(assertionError).hasMessage(actualIsEmpty());
  }

  @Test
  void should_fail_throwing_npe_if_assert_factory_is_null() {
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(iterable).first(null));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage(shouldNotBeNull("instanceOfAssertFactory").create());
  }

  @Test
  void should_pass_allowing_type_narrowed_assertions_if_first_element_is_an_instance_of_the_factory_type() {
    // WHEN
    AbstractStringAssert<?> result = assertThat(iterable).first(STRING);
    // THEN
    result.startsWith("str");
  }

  @Test
  void should_fail_if_first_element_is_not_an_instance_of_the_factory_type() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(iterable).first(INTEGER));
    // THEN
    then(assertionError).hasMessageContainingAll("Expecting:", "to be an instance of:", "but was instance of:");
  }

}
