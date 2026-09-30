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
package org.assertj.tests.core.api.uri;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.uri.ShouldHaveParameter.shouldHaveNoParameter;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.net.URI;
import java.util.List;

import org.junit.jupiter.api.Test;

class UriAssert_hasNoParameter_String_String_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    URI uri = null;
    String name = "article";
    String value = "10";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(uri).hasNoParameter(name, value));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_parameter_without_value_is_missing() {
    // GIVEN
    URI uri = URI.create("http://assertj.org/news");
    // WHEN/THEN
    assertThat(uri).hasNoParameter("article", null);
  }

  @Test
  void should_pass_if_parameter_with_value_is_missing() {
    // GIVEN
    URI uri = URI.create("http://assertj.org/news");
    // WHEN/THEN
    assertThat(uri).hasNoParameter("article", "10");
  }

  @Test
  void should_pass_if_parameter_with_value_is_present_without_value() {
    // GIVEN
    URI uri = URI.create("http://assertj.org/news?article");
    // WHEN/THEN
    assertThat(uri).hasNoParameter("article", "10");
  }

  @Test
  void should_pass_if_parameter_with_value_is_present_with_wrong_value() {
    // GIVEN
    URI uri = URI.create("http://assertj.org/news?article=11");
    // WHEN/THEN
    assertThat(uri).hasNoParameter("article", "10");
  }

  @Test
  void should_fail_if_parameter_without_value_is_present() {
    // GIVEN
    URI uri = URI.create("http://assertj.org/news?article");
    String name = "article";
    String expectedValue = null;
    List<String> actualValues = newArrayList((String) null);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(uri).hasNoParameter(name, expectedValue));
    // THEN
    then(assertionError).hasMessage(shouldHaveNoParameter(uri, name, expectedValue, actualValues).create());
  }

  @Test
  void should_fail_if_parameter_with_value_is_present() {
    // GIVEN
    URI uri = URI.create("http://assertj.org/news?article=10");
    String name = "article";
    String expectedValue = "10";
    List<String> actualValue = newArrayList("10");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(uri).hasNoParameter(name, expectedValue));
    // THEN
    then(assertionError).hasMessage(shouldHaveNoParameter(uri, name, expectedValue, actualValue).create());
  }

}
