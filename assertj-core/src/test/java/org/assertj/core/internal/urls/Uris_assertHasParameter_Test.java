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
package org.assertj.core.internal.urls;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.uri.ShouldHaveParameter.shouldHaveParameter;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.list;

import java.net.URI;

import org.assertj.core.internal.UrisBaseTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class Uris_assertHasParameter_Test extends UrisBaseTest {

  @ParameterizedTest
  @ValueSource(strings = {
      "http://assertj.org/news?article",
      "http://assertj.org/news?article=10",
  })
  void should_pass_if_parameter_is_found(URI uri) {
    // WHEN/THEN
    uris.assertHasParameter(info, uri, "article");
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "http://assertj.org/news",
      "http://assertj.org/news?story=1",
  })
  void should_fail_if_parameter_is_missing(URI uri) {
    // GIVEN
    String name = "article";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasParameter(info, uri, name));
    // THEN
    then(assertionError).hasMessage(shouldHaveParameter(uri, name).create());
  }

  @ParameterizedTest
  @CsvSource({
      "http://assertj.org/news?article,                 ",            // expected null
      "http://assertj.org/news?article&article,         ",            // expected null
      "http://assertj.org/news?article=,                ''",          // expected empty
      "http://assertj.org/news?article=10,              10",          // expected plain
      "http://assertj.org/news?article=abc%26page%3D5,  abc&page=5",  // expected escaped
  })
  void should_pass_if_parameter_with_expected_value_is_found(URI uri, String expected) {
    // WHEN/THEN
    uris.assertHasParameter(info, uri, "article", expected);
  }

  @ParameterizedTest
  @CsvSource({
      "http://assertj.org/news, ",            // expected null
      "http://assertj.org/news, ''",          // expected empty
      "http://assertj.org/news, 10",          // expected plain
      "http://assertj.org/news, abc&page=5",  // expected escaped
  })
  void should_fail_if_parameter_with_expected_value_is_missing(URI uri, String expected) {
    // GIVEN
    String name = "article";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasParameter(info, uri, name, expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveParameter(uri, name, expected).create());
  }

  @ParameterizedTest
  @CsvSource({
      "http://assertj.org/news?article,     10, ",    // expected not null, actual null
      "http://assertj.org/news?article=,    10, ''",  // expected not null, actual empty
      "http://assertj.org/news?article=11,  ,   11",  // expected null, actual not null
      "http://assertj.org/news?article=11,  '', 11",  // expected empty, actual not null
      "http://assertj.org/news?article=11,  10, 11",  // expected not matching actual
  })
  void should_fail_if_parameter_has_wrong_value(URI uri, String expected, String actual) {
    // GIVEN
    String name = "article";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasParameter(info, uri, name, expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveParameter(uri, name, expected, list(actual)).create());
  }

  @ParameterizedTest
  @CsvSource({
      "http://assertj.org/news?article&article,       10, ,   ",    // expected not null, actuals null
      "http://assertj.org/news?article=11&article=12, ,   11, 12",  // expected null, actuals not null
  })
  void should_fail_if_parameter_has_multiple_wrong_values(URI uri, String expected, String actual1, String actual2) {
    // GIVEN
    String name = "article";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasParameter(info, uri, name, expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveParameter(uri, name, expected, list(actual1, actual2)).create());
  }

}
