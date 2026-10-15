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
package org.assertj.tests.core.api.url;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.uri.ShouldHaveParameter.shouldHaveNoParameter;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.net.URI;
import java.net.URL;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class UrlAssert_hasNoParameter_String_String_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    URL actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasNoParameter("article", "10"));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_parameter_does_not_have_unwanted_value() throws Exception {
    // GIVEN
    URL actual = URI.create("http://example.com?article=10").toURL();
    // WHEN/THEN
    assertThat(actual).hasNoParameter("article", "11");
  }

  @Test
  void should_fail_if_parameter_has_unwanted_value() throws Exception {
    // GIVEN
    URL actual = URI.create("http://example.com?article=10").toURL();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasNoParameter("article", "10"));
    // THEN
    then(error).hasMessage(shouldHaveNoParameter(actual, "article", "10", Arrays.asList("10")).create());
  }

  @Test
  void should_distinguish_absent_and_empty_values() throws Exception {
    // GIVEN
    URL actual = URI.create("http://example.com?article=").toURL();
    // WHEN/THEN
    assertThat(actual).hasNoParameter("article", null);
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = "10")
  void should_pass_if_unwanted_value_is_not_among_repeated_values(String value) throws Exception {
    // GIVEN
    URL actual = URI.create("http://example.com?article=11&article=12").toURL();
    // WHEN/THEN
    assertThat(actual).hasNoParameter("article", value);
  }

  @Test
  void should_fail_if_unwanted_value_is_among_repeated_values() throws Exception {
    // GIVEN
    URL actual = URI.create("http://example.com?article=11&article=10&article").toURL();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasNoParameter("article", "10"));
    // THEN
    then(error).hasMessage(shouldHaveNoParameter(actual, "article", "10", Arrays.asList("11", "10", null)).create());
  }

  @Test
  void should_fail_if_unwanted_null_value_is_among_repeated_values() throws Exception {
    // GIVEN
    URL actual = URI.create("http://example.com?article=11&article").toURL();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasNoParameter("article", null));
    // THEN
    then(error).hasMessage(shouldHaveNoParameter(actual, "article", null, Arrays.asList("11", null)).create());
  }

}
