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
package org.assertj.core.api.url;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.junit.jupiter.api.Test;

class UrlAssert_parameters_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    URL actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).parameters());
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_parameters_are_exactly_the_expected_ones() throws MalformedURLException {
    // GIVEN
    URL actual = new URL("http://www.helloworld.org/index.html?lang=en&tag=java&tag=test");
    // WHEN/THEN
    then(actual).parameters()
                .containsOnly(entry("tag", List.of("java", "test")),
                              entry("lang", List.of("en")));
  }

  @Test
  void should_fail_if_parameters_contain_an_unexpected_one() throws MalformedURLException {
    // GIVEN
    URL actual = new URL("http://www.helloworld.org/index.html?lang=en&debug=true");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).parameters()
                                                                      .containsOnly(entry("lang", List.of("en"))));
    // THEN
    then(assertionError).hasMessageContainingAll("map entries were unexpected", "\"debug\"=[\"true\"]");
  }

  @Test
  void should_pass_if_url_has_no_query() throws MalformedURLException {
    // GIVEN
    URL actual = new URL("http://www.helloworld.org/index.html");
    // WHEN/THEN
    then(actual).parameters().isEmpty();
  }

  @Test
  void should_distinguish_absent_value_from_empty_value() throws MalformedURLException {
    // GIVEN
    URL actual = new URL("http://www.helloworld.org/index.html?flag&empty=");
    // WHEN/THEN
    then(actual).parameters()
                .containsOnly(entry("flag", singletonList(null)),
                              entry("empty", List.of("")));
  }

  @Test
  void should_decode_parameter_names_and_values() throws MalformedURLException {
    // GIVEN
    URL actual = new URL("http://www.helloworld.org/index.html?first%20name=J%C3%B6rg%26Co");
    // WHEN/THEN
    then(actual).parameters()
                .containsOnly(entry("first name", List.of("Jörg&Co")));
  }
}
