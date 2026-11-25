/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.objectarray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.THROWABLE;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.ShouldStartWith.shouldStartWith;
import static org.assertj.core.error.ZippedElementsShouldSatisfy.zippedElementsShouldSatisfy;
import static org.assertj.tests.core.testkit.TestData.someInfo;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;
import java.util.function.BiConsumer;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.error.ZippedElementsShouldSatisfy.ZipSatisfyError;
import org.junit.jupiter.api.Test;

class ObjectArrayAssert_zipSatisfy_Test {

  @Test
  void should_pass_if_all_zipped_pairs_satisfy_requirements() {
    // GIVEN
    String[] actual = { "Luke", "Yoda", "Leia" };
    String[] other = { "LUKE", "YODA", "LEIA" };
    BiConsumer<String, String> requirements = (s1, s2) -> {
      assertThat(s1).isEqualToIgnoringCase(s2);
      assertThat(s1).startsWith(s2.substring(0, 1));
    };
    // WHEN/THEN
    then(actual).zipSatisfy(other, requirements);
  }

  @Test
  void should_pass_if_both_arrays_are_empty() {
    assertThat(new String[0]).zipSatisfy(new String[0], (s1, s2) -> assertThat(s1).isEqualToIgnoringCase(s2));
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String[] actual = null;
    String[] other = { "LUKE", "YODA", "LEIA" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).zipSatisfy(other,
                                                                                  (s1,
                                                                                   s2) -> assertThat(s1).isEqualToIgnoringCase(s2)));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_requirements_is_null() {
    // GIVEN
    String[] actual = { "Luke", "Yoda", "Leia" };
    String[] other = { "LUKE", "YODA", "LEIA" };
    BiConsumer<String, String> requirements = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(actual).zipSatisfy(other, requirements));
    // THEN
    then(exception).hasMessage("The BiConsumer expressing the assertions requirements must not be null");
  }

  @Test
  void should_throw_error_if_other_is_null() {
    // GIVEN
    String[] actual = { "Luke", "Yoda", "Leia" };
    String[] other = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(actual).zipSatisfy(other,
                                                                                  (s1,
                                                                                   s2) -> assertThat(s1).isEqualToIgnoringCase(s2)));
    // THEN
    then(exception).hasMessage("The iterable to zip actual with must not be null");
  }

  @Test
  void should_fail_if_arrays_have_different_sizes() {
    // GIVEN
    String[] actual = { "Luke", "Yoda", "Leia" };
    String[] other = { "LUKE", "YODA", "LEIA", "VADER" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).zipSatisfy(other,
                                                                                  (s1, s2) -> assertThat(s1).startsWith(s2)));
    // THEN
    then(assertionError).hasMessage(shouldHaveSameSizeAs(List.of(actual), List.of(other), 3, 4).create());
  }

  @Test
  void should_fail_if_zipped_pairs_do_not_satisfy_requirements() {
    // GIVEN
    String[] actual = { "Luke", "Yoda", "Leia" };
    String[] other = { "LUKE", "YODA", "LEIA" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).zipSatisfy(other,
                                                                                  (s1, s2) -> assertThat(s1).startsWith(s2)));
    // THEN
    List<ZipSatisfyError> errors = List.of(new ZipSatisfyError("Luke", "LUKE", shouldStartWith("Luke", "LUKE").create()),
                                           new ZipSatisfyError("Yoda", "YODA", shouldStartWith("Yoda", "YODA").create()),
                                           new ZipSatisfyError("Leia", "LEIA", shouldStartWith("Leia", "LEIA").create()));
    then(assertionError).hasMessage(zippedElementsShouldSatisfy(someInfo(), List.of(actual), List.of(other), errors).create());
  }

  @Test
  void should_work_with_soft_assertions() {
    // GIVEN
    SoftAssertions softly = new SoftAssertions();
    String[] other = { "LUKE", "YODA" };
    BiConsumer<Object, String> requirements = (o1, o2) -> assertThat(o1).hasSameHashCodeAs(o2);
    // WHEN
    softly.assertThat(new String[] { "Luke", "Yoda" })
          .overridingErrorMessage("error message")
          .zipSatisfy(other, requirements);
    // THEN
    then(softly.assertionErrorsCollected()).singleElement(THROWABLE).hasMessage("error message");
  }

}
