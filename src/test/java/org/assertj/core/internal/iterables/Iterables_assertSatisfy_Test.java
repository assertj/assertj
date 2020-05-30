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
package org.assertj.core.internal.iterables;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;

import org.assertj.core.internal.IterablesBaseTest;
import org.junit.jupiter.api.Test;

public class Iterables_assertSatisfy_Test extends IterablesBaseTest {

  private List<String> actual = newArrayList("Luke", "Leia", "Yoda");

  @Test
  public void should_pass_if_satisfy_single_requirement() {
    iterables.assertSatisfy(info, actual, s -> assertThat(s.length()).isEqualTo(4));
  }

  @Test
  public void should_pass_if_satisfy_multiple_requirements() {
    iterables.assertSatisfy(info, actual, s -> {
      assertThat(s.length()).isEqualTo(4);
      assertThat(s).doesNotContain("V");
    });
  }

  // TODO: after the failure message part is discussed, finish "should fail" part.
  // @Test
  // public void should_fail_according_to_requirements() {
  // // GIVEN
  // Consumer<String> restrictions = s -> {
  // assertThat(s.length()).isEqualTo(4);
  // assertThat(s).startsWith("L");
  // };
  //
  // // WHEN
  // AssertionError assertionError =expectAssertionError(() -> iterables.assertSatisfy(info, actual, restrictions));
  //
  // // THEN
  // List<UnsatisfiedRequirement> errors = list(new UnsatisfiedRequirement("Yoda", format("%n" +
  // "Expecting:%n" +
  // " <\"Yoda\">%n" +
  // "to start with:%n" +
  // " <\"L\">%n")));
  // // verify(failures).failure(info, elementsShouldSatisfy(actual, errors, info));
  //
  // // THEN
  // then(assertionError).hasMessage(elementsShouldSatisfy(actual, errors, info).create());
  // }
  //
  // @Test
  // public void should_fail_if_consumer_is_null() {
  // assertThatNullPointerException().isThrownBy(() -> assertThat(actual).satisfy(null))
  // .withMessage("The Consumer<T> expressing the assertions requirements must not be null");
  // }
  //
  // @Test
  // public void should_fail_if_actual_is_null() {
  // assertThatExceptionOfType(AssertionError.class).isThrownBy(() ->{
  // actual = null;
  // assertThat(actual).satisfy(null);
  // }).withMessage(actualIsNull());
  // }
}
