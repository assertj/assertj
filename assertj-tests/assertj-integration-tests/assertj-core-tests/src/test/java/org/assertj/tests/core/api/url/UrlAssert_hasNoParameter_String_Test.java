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

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.uri.ShouldHaveParameter.shouldHaveNoParameter;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.net.URI;
import java.net.URL;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class UrlAssert_hasNoParameter_String_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    URL actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasNoParameter("article"));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_parameter_is_absent() throws Exception {
    // GIVEN
    URL actual = URI.create("http://example.com?story=1").toURL();
    // WHEN/THEN
    assertThat(actual).hasNoParameter("article");
  }

  @Test
  void should_fail_if_parameter_is_present() throws Exception {
    // GIVEN
    URL actual = URI.create("http://example.com?article=10").toURL();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasNoParameter("article"));
    // THEN
    then(error).hasMessage(shouldHaveNoParameter(actual, "article", singletonList("10")).create());
  }

  @Test
  void should_fail_if_parameter_is_present_without_value() throws Exception {
    // GIVEN
    URL actual = URI.create("http://example.com?article").toURL();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasNoParameter("article"));
    // THEN
    then(error).hasMessage(shouldHaveNoParameter(actual, "article", singletonList(null)).create());
  }

  @Test
  void should_fail_if_parameter_is_present_multiple_times() throws Exception {
    // GIVEN
    URL actual = URI.create("http://example.com?article&article=10").toURL();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasNoParameter("article"));
    // THEN
    then(error).hasMessage(shouldHaveNoParameter(actual, "article", Arrays.asList(null, "10")).create());
  }

}
