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
package org.assertj.tests.core.api.iterable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.ShouldStartWith.shouldStartWith;
import static org.assertj.core.error.ZippedElementsShouldSatisfy.zippedElementsShouldSatisfy;
import static org.assertj.tests.core.testkit.TestData.someInfo;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;
import java.util.function.BiConsumer;

import org.assertj.core.error.ZippedElementsShouldSatisfy.ZipSatisfyError;
import org.junit.jupiter.api.Test;

class IterableAssert_zipSatisfy_Test {

  private final List<String> actual = List.of("Luke", "Yoda", "Leia");
  private final List<String> other = List.of("LUKE", "YODA", "LEIA");

  @Test
  void should_satisfy_single_zip_requirement() {
    assertThat(actual).zipSatisfy(other, (s1, s2) -> assertThat(s1).isEqualToIgnoringCase(s2));
  }

  @Test
  void should_satisfy_compound_zip_requirements() {
    // GIVEN
    BiConsumer<String, String> zipRequirements = (s1, s2) -> {
      assertThat(s1).isEqualToIgnoringCase(s2);
      assertThat(s1).startsWith(firstChar(s2));
    };
    // WHEN/THEN
    then(actual).zipSatisfy(other, zipRequirements);
  }

  @Test
  void should_pass_if_both_iterables_are_empty() {
    assertThat(List.<String> of()).zipSatisfy(List.<String> of(), (s1, s2) -> assertThat(s1).isEqualToIgnoringCase(s2));
  }

  @Test
  void should_fail_according_to_requirements() {
    // GIVEN
    BiConsumer<String, String> zipRequirements = (s1, s2) -> assertThat(s1).startsWith(s2);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).zipSatisfy(other, zipRequirements));
    // THEN
    List<ZipSatisfyError> errors = List.of(new ZipSatisfyError("Luke", "LUKE", shouldStartWith("Luke", "LUKE").create()),
                                           new ZipSatisfyError("Yoda", "YODA", shouldStartWith("Yoda", "YODA").create()),
                                           new ZipSatisfyError("Leia", "LEIA", shouldStartWith("Leia", "LEIA").create()));
    then(assertionError).hasMessage(zippedElementsShouldSatisfy(someInfo(), actual, other, errors).create());
  }

  @Test
  void should_fail_when_compared_iterables_have_different_sizes() {
    // GIVEN
    List<String> other = List.of("LUKE", "YODA", "LEIA", "Vader");
    BiConsumer<String, String> zipRequirements = (s1, s2) -> assertThat(s1).startsWith(s2);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).zipSatisfy(other, zipRequirements));
    // THEN
    then(assertionError).hasMessage(shouldHaveSameSizeAs(actual, other, actual.size(), other.size()).create());
  }

  @Test
  void should_fail_if_consumer_is_null() {
    // GIVEN
    BiConsumer<String, String> zipRequirements = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).zipSatisfy(other, zipRequirements));
    // THEN
    then(npe).hasMessage("The BiConsumer expressing the assertions requirements must not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    BiConsumer<String, String> zipRequirements = (s1, s2) -> assertThat(s1).isEqualToIgnoringCase(s2);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).zipSatisfy(other, zipRequirements));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_other_is_null() {
    // GIVEN
    List<String> other = null;
    BiConsumer<String, String> zipRequirements = (s1, s2) -> assertThat(s1).isEqualToIgnoringCase(s2);
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).zipSatisfy(other, zipRequirements));
    // THEN
    then(npe).hasMessage("The iterable to zip actual with must not be null");
  }

  private static String firstChar(String s) {
    return String.valueOf(s.charAt(0));
  }

}
