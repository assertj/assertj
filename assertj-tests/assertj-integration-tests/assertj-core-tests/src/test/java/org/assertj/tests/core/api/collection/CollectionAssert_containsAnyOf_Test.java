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
package org.assertj.tests.core.api.collection;

import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpHeaders;

class CollectionAssert_containsAnyOf_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Collection<String> actual = null;
    String[] values = array("foo", "bar");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).containsAnyOf(values));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_given_array_is_null() {
    // GIVEN
    Collection<String> actual = List.of("foo");
    String[] values = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).containsAnyOf(values));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class).hasMessage(shouldNotBeNull("values").create());
  }

  @Test
  void should_pass_if_given_array_is_empty() {
    // GIVEN
    Collection<String> actual = List.of("foo");
    String[] values = array();
    // WHEN/THEN
    assertThat(actual).containsAnyOf(values);
  }

  @ParameterizedTest
  @MethodSource
  void should_pass_with_case_insensitive_set(Collection<String> actual) {
    // WHEN/THEN
    assertThat(actual).containsAnyOf("fOo", "bar");
  }

  static Stream<?> should_pass_with_case_insensitive_set() {
    Set<String> caseInsensitiveTreeSet = new TreeSet<>(CASE_INSENSITIVE_ORDER);
    caseInsensitiveTreeSet.add("Foo");

    HttpHeaders springHttpHeaders = new HttpHeaders();
    springHttpHeaders.add("Foo", "value");
    Set<String> springHttpHeaderNames = springHttpHeaders.headerNames();

    return Stream.of(caseInsensitiveTreeSet, springHttpHeaderNames);
  }

}
