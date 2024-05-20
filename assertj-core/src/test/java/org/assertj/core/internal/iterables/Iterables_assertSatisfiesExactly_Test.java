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
package org.assertj.core.internal.iterables;

import static com.google.common.collect.Maps.newHashMap;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ElementsShouldSatisfy.elementsShouldSatisfyExactly;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.error.ShouldStartWith.shouldStartWith;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Maps.newHashMap;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.assertj.core.error.UnsatisfiedRequirement;
import org.assertj.core.internal.IterablesBaseTest;
import org.junit.jupiter.api.Test;

class Iterables_assertSatisfiesExactly_Test extends IterablesBaseTest {

  private List<String> actual = list("Luke", "Leia", "Yoda");
  private Consumer<Object>[] requirements = array(element -> assertThat(element).isNotNull());

  @Test
  void should_pass_when_each_element_satisfies_its_given_requirements() {
    // GIVEN
    Consumer<String>[] requirements = array(name -> assertThat(name).isNotBlank(),
                                            name -> assertThat(name).startsWith("Lei"),
                                            name -> assertThat(name).endsWith("da"));
    // WHEN/THEN
    iterables.assertSatisfiesExactly(info, actual, requirements);
  }

  @Test
  void should_pass_when_both_actual_and_requirements_are_empty() {
    // GIVEN
    Consumer<String>[] requirements = array();
    actual.clear();
    // WHEN/THEN
    iterables.assertSatisfiesExactly(info, actual, requirements);
  }

  @Test
  void should_fail_when_any_element_is_not_satisfying_its_requirements() {
    // GIVEN
    Consumer<String>[] requirements = array(name -> assertThat(name).isNotBlank(),
                                            name -> assertThat(name).startsWith("Han"),
                                            name -> assertThat(name).endsWith("da"));
    // WHEN
    expectAssertionError(() -> iterables.assertSatisfiesExactly(info, actual, requirements));
    // THEN
    UnsatisfiedRequirement re = new UnsatisfiedRequirement("Leia", shouldStartWith("Leia", "Han").create());
    verify(failures).failure(info, elementsShouldSatisfyExactly(actual, newHashMap(1, re), info));
  }

  @Test
  void should_fail_when_multiple_elements_are_not_satisfying_their_respective_requirements() {
    // GIVEN
    Consumer<String>[] requirements = array(name -> assertThat(name).isNotBlank(),
                                            name -> assertThat(name).startsWith("Han"),
                                            name -> assertThat(name).startsWith("Io"));
    // WHEN
    expectAssertionError(() -> iterables.assertSatisfiesExactly(info, actual, requirements));
    // THEN
    Map<Integer, UnsatisfiedRequirement> unsatisfiedRequirements = newHashMap();
    unsatisfiedRequirements.put(1, new UnsatisfiedRequirement("Leia", shouldStartWith("Leia", "Han").create()));
    unsatisfiedRequirements.put(2, new UnsatisfiedRequirement("Yoda", shouldStartWith("Yoda", "Io").create()));
    verify(failures).failure(info, elementsShouldSatisfyExactly(actual, unsatisfiedRequirements, info));
  }

  @Test
  void should_fail_when_requirements_are_met_but_in_the_right_order() {
    // GIVEN
    Consumer<String>[] requirements = array(name -> assertThat(name).isNotBlank(),
                                            name -> assertThat(name).startsWith("Yo"),
                                            name -> assertThat(name).startsWith("Lei"));
    // WHEN
    expectAssertionError(() -> iterables.assertSatisfiesExactly(info, actual, requirements));
    // THEN
    Map<Integer, UnsatisfiedRequirement> unsatisfiedRequirements = newHashMap();
    unsatisfiedRequirements.put(1, new UnsatisfiedRequirement("Leia", shouldStartWith("Leia", "Yo").create()));
    unsatisfiedRequirements.put(2, new UnsatisfiedRequirement("Yoda", shouldStartWith("Yoda", "Lei").create()));
    verify(failures).failure(info, elementsShouldSatisfyExactly(actual, unsatisfiedRequirements, info));
  }

  @Test
  void should_fail_when_actual_and_requirements_have_different_sizes() {
    // WHEN
    AssertionError error = expectAssertionError(() -> iterables.assertSatisfiesExactly(info, actual, requirements));
    // THEN
    then(error).hasMessage(shouldHaveSameSizeAs(actual, requirements, actual.size(), requirements.length).create());
  }

  @Test
  void should_fail_if_requirements_is_null() {
    // GIVEN
    Consumer<Object>[] requirements = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> iterables.assertSatisfiesExactly(info, actual, requirements));
    // THEN
    then(error).hasMessage(format("%nExpecting an array but was: null"));
  }

  @Test
  void should_fail_when_actual_is_null() {
    // GIVEN
    List<Object> actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> iterables.assertSatisfiesExactly(info, actual, requirements));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

}
