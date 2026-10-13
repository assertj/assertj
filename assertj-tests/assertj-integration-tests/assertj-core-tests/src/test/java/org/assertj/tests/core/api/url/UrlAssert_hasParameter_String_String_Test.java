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
import static org.assertj.core.error.uri.ShouldHaveParameter.shouldHaveParameter;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.net.URI;
import java.net.URL;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class UrlAssert_hasParameter_String_String_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    URL actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasParameter("article", "10"));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_parameter_has_expected_value() throws Exception {
    // GIVEN
    URL actual = URI.create("http://example.com?article=10").toURL();
    // WHEN/THEN
    assertThat(actual).hasParameter("article", "10");
  }

  @Test
  void should_pass_if_parameter_has_no_value_and_null_is_expected() throws Exception {
    // GIVEN
    URL actual = URI.create("http://example.com?article").toURL();
    // WHEN/THEN
    assertThat(actual).hasParameter("article", null);
  }

  @Test
  void should_fail_if_parameter_value_does_not_match() throws Exception {
    // GIVEN
    URL actual = URI.create("http://example.com?article=11").toURL();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasParameter("article", "10"));
    // THEN
    then(error).hasMessage(shouldHaveParameter(actual, "article", "10", Arrays.asList("11")).create());
  }

  @Test
  void should_fail_if_parameter_is_missing() throws Exception {
    // GIVEN
    URL actual = URI.create("http://example.com").toURL();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasParameter("article", "10"));
    // THEN
    then(error).hasMessage(shouldHaveParameter(actual, "article", "10").create());
  }

  @Test
  void should_fail_if_null_value_expected_and_parameter_is_missing() throws Exception {
    // GIVEN
    URL actual = URI.create("http://example.com").toURL();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasParameter("article", null));
    // THEN
    then(error).hasMessage(shouldHaveParameter(actual, "article", (String) null).create());
  }

  @Test
  void should_fail_if_null_value_expected_and_parameter_has_value() throws Exception {
    // GIVEN
    URL actual = URI.create("http://example.com?article=11").toURL();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasParameter("article", null));
    // THEN
    then(error).hasMessage(shouldHaveParameter(actual, "article", null, Arrays.asList("11")).create());
  }

  @Test
  void should_fail_if_null_value_expected_and_parameter_has_multiple_values() throws Exception {
    // GIVEN
    URL actual = URI.create("http://example.com?article=11&article=12").toURL();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasParameter("article", null));
    // THEN
    then(error).hasMessage(shouldHaveParameter(actual, "article", null, Arrays.asList("11", "12")).create());
  }

  @Test
  void should_fail_if_value_expected_and_parameter_has_no_value() throws Exception {
    // GIVEN
    URL actual = URI.create("http://example.com?article").toURL();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasParameter("article", "10"));
    // THEN
    then(error).hasMessage(shouldHaveParameter(actual, "article", "10", Arrays.asList((String) null)).create());
  }

  @Test
  void should_fail_if_value_expected_and_parameter_repeated_without_value() throws Exception {
    // GIVEN
    URL actual = URI.create("http://example.com?article&article").toURL();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasParameter("article", "10"));
    // THEN
    then(error).hasMessage(shouldHaveParameter(actual, "article", "10", Arrays.asList(null, null)).create());
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = "10")
  void should_pass_if_one_of_repeated_values_matches(String value) throws Exception {
    // GIVEN
    URL actual = URI.create("http://example.com?article=11&article=10&article").toURL();
    // WHEN/THEN
    assertThat(actual).hasParameter("article", value);
  }

}
