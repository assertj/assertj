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
package org.assertj.core.internal.iterables;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.error.ShouldStartWith.shouldStartWith;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;

import java.util.List;
import java.util.function.Consumer;
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
    AssertionError error = expectAssertionError(() -> iterables.assertSatisfiesExactly(info, actual, requirements));
    // THEN
    // can't build the exact error message due to internal stack traces
    then(error).hasMessageStartingWith(format("%n" +
                                              "Expecting each element of:%n" +
                                              "  %s%n" +
                                              "to satisfy the requirements at its index, but these elements did not:%n%n",
                                              info.representation().toStringOf(actual)))
               .hasMessageContaining(shouldStartWith("Leia", "Han").create());
  }

  @Test
  void should_fail_when_multiple_elements_are_not_satisfying_their_respective_requirements() {
    // GIVEN
    Consumer<String>[] requirements = array(name -> assertThat(name).isNotBlank(),
                                            name -> assertThat(name).startsWith("Han"),
                                            name -> assertThat(name).startsWith("Io"));
    // WHEN
    AssertionError error = expectAssertionError(() -> iterables.assertSatisfiesExactly(info, actual, requirements));
    // THEN
    // can't build the exact error message due to internal stack traces
    then(error).hasMessageStartingWith(format("%n" +
                                              "Expecting each element of:%n" +
                                              "  %s%n" +
                                              "to satisfy the requirements at its index, but these elements did not:%n%n",
                                              info.representation().toStringOf(actual)))
               .hasMessageContaining(shouldStartWith("Leia", "Han").create())
               .hasMessageContaining(shouldStartWith("Yoda", "Io").create());
  }

  @Test
  void should_fail_when_requirements_are_met_but_not_in_the_right_order() {
    // GIVEN
    Consumer<String>[] requirements = array(name -> assertThat(name).isNotBlank(),
                                            name -> assertThat(name).startsWith("Yo"),
                                            name -> assertThat(name).startsWith("Lei"));
    // WHEN
    // WHEN
    AssertionError error = expectAssertionError(() -> iterables.assertSatisfiesExactly(info, actual, requirements));
    // THEN
    // can't build the exact error message due to internal stack traces
    then(error).hasMessageStartingWith(format("%n" +
                                              "Expecting each element of:%n" +
                                              "  %s%n" +
                                              "to satisfy the requirements at its index, but these elements did not:%n%n",
                                              info.representation().toStringOf(actual)))
               .hasMessageContaining(shouldStartWith("Leia", "Yo").create())
               .hasMessageContaining(shouldStartWith("Yoda", "Lei").create());

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
    then(error).hasMessage("%nExpecting an array but was: null".formatted());
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
