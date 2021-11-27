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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.internal.urls;

import org.assertj.core.data.MapEntry;
import org.assertj.core.internal.UrlsBaseTest;
import org.assertj.core.test.Maps;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.uri.ShouldHaveParameter.shouldHaveSameParameters;
import static org.assertj.core.internal.ErrorMessages.setOfEntriesToLookForIsNull;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

class Urls_containsExactlyParameters_Entries_Test extends UrlsBaseTest {

  @Test
  void should_pass_if_parameters_are_same() throws MalformedURLException {
    // GIVEN
    URL URL = new URL("http://assertj.org?a=v1&a=v2&b=v1");
    Collection<Map.Entry<String, String>> EXPECTATION_INPUT = Lists.newArrayList(
      new AbstractMap.SimpleEntry<>("a", "v1"),
      new AbstractMap.SimpleEntry<>("a", "v2"),
      new AbstractMap.SimpleEntry<>("b", "v1")
    );

    // WHEN/THEN
    urls.assertContainsExactlyParameters(info, URL, EXPECTATION_INPUT);
  }

  @Test
  void should_pass_if_parameter_has_no_value_and_expected_is_null() throws MalformedURLException {
    // GIVEN
    URL URL = new URL("http://assertj.org?a&b=v1");
    Collection<Map.Entry<String, String>> EXPECTATION_INPUT = Lists.newArrayList(
      new AbstractMap.SimpleEntry<>("a", null),
      new AbstractMap.SimpleEntry<>("b", "v1")
    );

    // WHEN/THEN
    urls.assertContainsExactlyParameters(info, URL, EXPECTATION_INPUT);
  }

  @Test
  void should_pass_if_parameter_has_empty_value_and_expected_is_null() throws MalformedURLException {
    // GIVEN
    URL URL = new URL("http://assertj.org?a&a=v1&b=v1");
    Collection<Map.Entry<String, String>> EXPECTATION_INPUT = Lists.newArrayList(
      new AbstractMap.SimpleEntry<>("a", "v1"),
      new AbstractMap.SimpleEntry<>("a", null),
      new AbstractMap.SimpleEntry<>("b", "v1")
    );

    // WHEN/THEN
    urls.assertContainsExactlyParameters(info, URL, EXPECTATION_INPUT);
  }


  @Test
  void should_pass_if_input_is_empty() throws MalformedURLException {
    // GIVEN
    URL URL = new URL("http://assertj.org");
    Collection<Map.Entry<String, String>> EXPECTATION_INPUT = Lists.newArrayList();

    // WHEN/THEN
    urls.assertContainsExactlyParameters(info, URL, EXPECTATION_INPUT);
  }

  @Test
  void should_fail_if_input_is_null() throws MalformedURLException {
    // GIVEN
    URL URL = new URL("http://assertj.org?a=v1&a=v2&b=v1");

    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(() ->
      urls.assertContainsExactlyParameters(info, URL, (Collection<Map.Entry<String, String>>) null)
    ).withMessage(setOfEntriesToLookForIsNull());
  }

  @Test
  void should_fail_if_parameter_missing_and_expected_is_null() throws MalformedURLException {
    // GIVEN
    final Map<String, List<String>> URL_PARAMS = Maps.mapOf(
      MapEntry.entry("b", Lists.newArrayList("v1"))
    );
    URL URL = new URL("http://assertj.org?b=v1");

    final Map<String, List<String>> EXPECTED_PARAMS = Maps.mapOf(
      MapEntry.entry("a", Lists.newArrayList((String) null)),
      MapEntry.entry("b", Lists.newArrayList("v1"))
    );
    Collection<Map.Entry<String, String>> EXPECTATION_INPUT = Lists.newArrayList(
      new AbstractMap.SimpleEntry<>("a", null),
      new AbstractMap.SimpleEntry<>("b", "v1")
    );

    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertContainsExactlyParameters(info, URL, EXPECTATION_INPUT));

    // THEN
    final Map<String, List<String>> MISSING = Maps.mapOf(
      MapEntry.entry("a", Lists.newArrayList((String) null))
    );

    final Map<String, List<String>> NOT_EXPECTED = Maps.mapOf();

    then(assertionError).hasMessage(shouldHaveSameParameters(URL_PARAMS, EXPECTED_PARAMS, MISSING, NOT_EXPECTED).create());
  }

  @Test
  void should_fail_if_parameter_has_value_and_expected_is_null() throws MalformedURLException {
    // GIVEN
    final Map<String, List<String>> URL_PARAMS = Maps.mapOf(
      MapEntry.entry("a", Lists.newArrayList("v1")),
      MapEntry.entry("b", Lists.newArrayList("v1"))
    );
    URL URL = new URL("http://assertj.org?a=v1&b=v1");

    final Map<String, List<String>> EXPECTED_PARAMS = Maps.mapOf(
      MapEntry.entry("a", Lists.newArrayList((String) null)),
      MapEntry.entry("b", Lists.newArrayList("v1"))
    );
    Collection<Map.Entry<String, String>> EXPECTATION_INPUT = Lists.newArrayList(
      new AbstractMap.SimpleEntry<>("a", null),
      new AbstractMap.SimpleEntry<>("b", "v1")
    );

    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertContainsExactlyParameters(info, URL, EXPECTATION_INPUT));

    // THEN
    final Map<String, List<String>> MISSING = Maps.mapOf(
      MapEntry.entry("a", Lists.newArrayList((String) null))
    );

    final Map<String, List<String>> NOT_EXPECTED = Maps.mapOf(
      MapEntry.entry("a", Lists.newArrayList("v1"))
    );

    then(assertionError).hasMessage(shouldHaveSameParameters(URL_PARAMS, EXPECTED_PARAMS, MISSING, NOT_EXPECTED).create());
  }

  @Test
  void should_fail_if_expected_param_value_missing() throws MalformedURLException {
    // GIVEN
    final Map<String, List<String>> URL_PARAMS = Maps.mapOf(
      MapEntry.entry("a", Lists.newArrayList("v1")),
      MapEntry.entry("b", Lists.newArrayList("v1"))
    );
    URL URL = new URL("http://assertj.org?a=v1&b=v1");

    final Map<String, List<String>> EXPECTED_PARAMS = Maps.mapOf(
      MapEntry.entry("a", Lists.newArrayList("v1", "v2")),
      MapEntry.entry("b", Lists.newArrayList("v1"))
    );
    Collection<Map.Entry<String, String>> EXPECTATION_INPUT = Lists.newArrayList(
      new AbstractMap.SimpleEntry<>("a", "v1"),
      new AbstractMap.SimpleEntry<>("a", "v2"),
      new AbstractMap.SimpleEntry<>("b", "v1")
    );


    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertContainsExactlyParameters(info, URL, EXPECTATION_INPUT));

    // THEN
    final Map<String, List<String>> MISSING = Maps.mapOf(
      MapEntry.entry("a", Lists.newArrayList("v2"))
    );

    final Map<String, List<String>> NOT_EXPECTED = Maps.mapOf();

    then(assertionError).hasMessage(shouldHaveSameParameters(URL_PARAMS, EXPECTED_PARAMS, MISSING, NOT_EXPECTED).create());
  }

  @Test
  void should_fail_if_expected_param_missing() throws MalformedURLException {
    // GIVEN
    final Map<String, List<String>> URL_PARAMS = Maps.mapOf(
      MapEntry.entry("b", Lists.newArrayList("v1"))
    );
    URL URL = new URL("http://assertj.org?b=v1");

    final Map<String, List<String>> EXPECTED_PARAMS = Maps.mapOf(
      MapEntry.entry("a", Lists.newArrayList("v1", "v2")),
      MapEntry.entry("b", Lists.newArrayList("v1"))
    );
    Collection<Map.Entry<String, String>> EXPECTATION_INPUT = Lists.newArrayList(
      new AbstractMap.SimpleEntry<>("a", "v1"),
      new AbstractMap.SimpleEntry<>("a", "v2"),
      new AbstractMap.SimpleEntry<>("b", "v1")
    );


    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertContainsExactlyParameters(info, URL, EXPECTATION_INPUT));

    // THEN
    final Map<String, List<String>> MISSING = Maps.mapOf(
      MapEntry.entry("a", Lists.newArrayList("v1", "v2"))
    );

    final Map<String, List<String>> NOT_EXPECTED = Maps.mapOf();

    then(assertionError).hasMessage(shouldHaveSameParameters(URL_PARAMS, EXPECTED_PARAMS, MISSING, NOT_EXPECTED).create());
  }

  @Test
  void should_fail_if_contains_unexpected_param_value() throws MalformedURLException {
    // GIVEN
    final Map<String, List<String>> URL_PARAMS = Maps.mapOf(
      MapEntry.entry("a", Lists.newArrayList("v1", "v2")),
      MapEntry.entry("b", Lists.newArrayList("v1", "v2"))
    );
    URL URL = new URL("http://assertj.org?a=v1&a=v2&b=v1&b=v2");

    final Map<String, List<String>> EXPECTED_PARAMS = Maps.mapOf(
      MapEntry.entry("a", Lists.newArrayList("v1", "v2")),
      MapEntry.entry("b", Lists.newArrayList("v1"))
    );
    Collection<Map.Entry<String, String>> EXPECTATION_INPUT = Lists.newArrayList(
      new AbstractMap.SimpleEntry<>("a", "v1"),
      new AbstractMap.SimpleEntry<>("a", "v2"),
      new AbstractMap.SimpleEntry<>("b", "v1")
    );


    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertContainsExactlyParameters(info, URL, EXPECTATION_INPUT));

    // THEN
    final Map<String, List<String>> MISSING = Maps.mapOf();

    final Map<String, List<String>> NOT_EXPECTED = Maps.mapOf(
      MapEntry.entry("b", Lists.newArrayList("v2"))
    );

    then(assertionError).hasMessage(shouldHaveSameParameters(URL_PARAMS, EXPECTED_PARAMS, MISSING, NOT_EXPECTED).create());
  }

  @Test
  void should_fail_if_contains_unexpected_param() throws MalformedURLException {
    // GIVEN
    final Map<String, List<String>> URL_PARAMS = Maps.mapOf(
      MapEntry.entry("a", Lists.newArrayList("v1", "v2")),
      MapEntry.entry("b", Lists.newArrayList("v1", "v2"))
    );
    URL URL = new URL("http://assertj.org?a=v1&a=v2&b=v1&b=v2");

    final Map<String, List<String>> EXPECTED_PARAMS = Maps.mapOf(
      MapEntry.entry("a", Lists.newArrayList("v1", "v2"))
    );
    Collection<Map.Entry<String, String>> EXPECTATION_INPUT = Lists.newArrayList(
      new AbstractMap.SimpleEntry<>("a", "v1"),
      new AbstractMap.SimpleEntry<>("a", "v2")
    );


    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertContainsExactlyParameters(info, URL, EXPECTATION_INPUT));

    // THEN
    final Map<String, List<String>> MISSING = Maps.mapOf();

    final Map<String, List<String>> NOT_EXPECTED = Maps.mapOf(
      MapEntry.entry("b", Lists.newArrayList("v1, v2"))
    );

    then(assertionError).hasMessage(shouldHaveSameParameters(URL_PARAMS, EXPECTED_PARAMS, MISSING, NOT_EXPECTED).create());
  }

}

