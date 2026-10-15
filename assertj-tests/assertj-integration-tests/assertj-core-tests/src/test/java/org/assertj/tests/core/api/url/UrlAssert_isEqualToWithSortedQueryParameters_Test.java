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
import static org.assertj.core.error.uri.ShouldBeEqualToWithSortedQueryParameters.shouldBeEqualToWithSortedQueryParameters;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.net.URI;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class UrlAssert_isEqualToWithSortedQueryParameters_Test {

  @Test
  void should_fail_if_actual_is_null() throws Exception {
    // GIVEN
    URL actual = null;
    URL expected = URI.create("http://example.com").toURL();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).isEqualToWithSortedQueryParameters(expected));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_query_parameters_are_in_different_order() throws Exception {
    // GIVEN
    URL actual = URI.create("http://example.com?a=1&b=2").toURL();
    URL expected = URI.create("http://example.com?b=2&a=1").toURL();
    // WHEN/THEN
    assertThat(actual).isEqualToWithSortedQueryParameters(expected);
  }

  @ParameterizedTest
  @CsvSource({
      "https://example.com/path/to/page?color=purple&name=ferret#hello, https://example.com/path/to/page?color=purple&name=ferret#hello",
      "https://example.com/path/to/page?color=purple&name=ferret#hello, https://example.com/path/to/page?name=ferret&color=purple#hello",
      "https://example.com/path/to/page#hello, https://example.com/path/to/page#hello",
      "https://example.com/path/to/page, https://example.com/path/to/page"
  })
  void should_pass_if_urls_are_equivalent_regardless_of_parameters_order(String actualUrl, String expectedUrl) throws Exception {
    // GIVEN
    URL actual = URI.create(actualUrl).toURL();
    URL expected = URI.create(expectedUrl).toURL();
    // WHEN/THEN
    assertThat(actual).isEqualToWithSortedQueryParameters(expected);
  }

  @ParameterizedTest
  @CsvSource({
      "http://example.com/path/to/page?color=purple&name=ferret#hello, https://example.com/path/to/page?color=purple&name=ferret#hello",
      "https://example2.com/path/to/page?color=purple&name=ferret#hello, https://example.com/path/to/page?color=purple&name=ferret#hello",
      "https://example.com/sunt/to/ting?color=purple&name=ferret#hello, https://example.com/path/to/page?color=purple&name=ferret#hello",
      "https://example.com?color=purple&name=ferret#hello, https://example.com/path/to/page?color=purple&name=ferret#hello",
      "https://example.com/path/to/page?color=purple&name=ferret, https://example.com/path/to/page?color=red&name=ferret",
      "https://example.com/path/to/page?color=purple&name=ferret#world, https://example.com/path/to/page?color=purple&name=ferret#hello",
      "https://example.com/path/to/page?color=purple&name=ferret, https://example.com/path/to/page?color=purple&name=ferret#hello",
      "https://example.com/path/to/page?color=red&name=jackson#hello, https://example.com/path/to/page?color=purple&name=ferret#hello",
      "https://example.com/path/to/page?#hello, https://example.com/path/to/page?color=purple&name=ferret#hello",
      "https://example.com/path/to/page?color=purple&name=ferret#hello, https://example.com/path/to/page?"
  })
  void should_fail_if_urls_are_different_regardless_of_parameters_order(String actualUrl, String expectedUrl) throws Exception {
    // GIVEN
    URL actual = URI.create(actualUrl).toURL();
    URL expected = URI.create(expectedUrl).toURL();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).isEqualToWithSortedQueryParameters(expected));
    // THEN
    then(error).hasMessage(shouldBeEqualToWithSortedQueryParameters(actual, expected).create());
  }

}
