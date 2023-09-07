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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.guava.api;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.collect.TreeMultiset;
import org.assertj.core.error.ErrorMessageFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeUnmodifiable.shouldBeUnmodifiable;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.guava.testkit.AssertionErrors.expectAssertionError;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class MultisetAssert_isUnmodifiable_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Multiset<?> actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isUnmodifiable());
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @ParameterizedTest
  @MethodSource("modifiableMultisets")
  void should_fail_if_actual_can_be_modified(Multiset<?> actual, ErrorMessageFactory errorMessageFactory) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isUnmodifiable());
    // THEN
    then(assertionError).as(actual.getClass().getName())
                        .hasMessage(errorMessageFactory.create());
  }

  private static Stream<Arguments> modifiableMultisets() {
    return Stream.of(arguments(TreeMultiset.create(), shouldBeUnmodifiable("Collection.add(null)"),
                               HashMultiset.create(), shouldBeUnmodifiable("Collection.add(null)")));
  }

  @ParameterizedTest
  @MethodSource("unodifiableMultisets")
  void should_pass_if_actual_is_unmodifiable(Multiset<String> actual) {
    assertThatNoException().as(actual.getClass().getName())
                           .isThrownBy(() -> assertThat(actual).isUnmodifiable());
  }

  private static Stream<Multiset<String>> unodifiableMultisets() {
    Multiset<String> nonEmptyModifiable = HashMultiset.create();
    nonEmptyModifiable.add("a");

    return Stream.of(
                     Multisets.unmodifiableMultiset(HashMultiset.create()),
                     Multisets.unmodifiableMultiset(nonEmptyModifiable),
                     ImmutableMultiset.of(),
                     ImmutableMultiset.of("a"));
  }
}
