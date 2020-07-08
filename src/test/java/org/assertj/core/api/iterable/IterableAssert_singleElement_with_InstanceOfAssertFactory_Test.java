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
package org.assertj.core.api.iterable;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.INTEGER;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.list;

import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.InstanceOfAssertFactory;
import org.assertj.core.api.IterableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link IterableAssert#singleElement(InstanceOfAssertFactory)}</code>.
 * 
 * @author Michael Grafl
 */
@DisplayName("IterableAssert singleElement(InstanceOfAssertFactory)")
class IterableAssert_singleElement_with_InstanceOfAssertFactory_Test {

  private final Iterable<String> babySimpsons = list("Maggie");
  private final Iterable<String> simpsons = list("Homer", "Marge", "Lisa", "Bart", "Maggie");

  @Test
  void should_fail_if_iterable_is_empty() {
    // GIVEN
    Iterable<String> iterable = emptyList();
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(iterable).singleElement(STRING));
    // THEN
    then(assertionError).hasMessageContaining("Expected size:<1> but was:<0>");
  }

  @Test
  void should_fail_throwing_npe_if_assert_factory_is_null() {
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(babySimpsons).singleElement(null));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage(shouldNotBeNull("instanceOfAssertFactory").create());
  }

  @Test
  void should_pass_allowing_type_narrowed_assertions_if_first_element_is_an_instance_of_the_factory_type() {
    // WHEN
    AbstractStringAssert<?> result = assertThat(babySimpsons).singleElement(STRING);
    // THEN
    result.startsWith("Mag");
  }

  @Test
  void should_fail_if_first_element_is_not_an_instance_of_the_factory_type() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(babySimpsons).singleElement(INTEGER));
    // THEN
    then(assertionError).hasMessageContainingAll("Expecting:", "to be an instance of:", "but was instance of:");
  }

  @Test
  void should_fail_if_iterable_has_more_than_one_element() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(simpsons).singleElement(STRING));
    // THEN
    then(assertionError).hasMessageContaining("Expected size:<1> but was:<5>");
  }


}
