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
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.uri.ShouldHavePath.shouldHavePath;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.net.URI;
import java.net.URL;

import org.junit.jupiter.api.Test;

class UrlAssert_hasPath_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    URL actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasPath("/pages"));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_has_expected_path() throws Exception {
    // GIVEN
    URL actual = URI.create("http://example.com/pages").toURL();
    // WHEN/THEN
    assertThat(actual).hasPath("/pages");
  }

  @Test
  void should_fail_if_actual_has_different_path() throws Exception {
    // GIVEN
    URL actual = URI.create("http://example.com/pages").toURL();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasPath("/news"));
    // THEN
    then(error).hasMessage(shouldHavePath(actual, "/news").create());
  }

  @Test
  void should_throw_if_expected_path_is_null() throws Exception {
    // GIVEN
    URL actual = URI.create("http://example.com/pages").toURL();
    String expected = null;
    // WHEN
    NullPointerException exception = catchNullPointerException(() -> assertThat(actual).hasPath(expected));
    // THEN
    then(exception).hasMessage(shouldNotBeNull("expected").create());
  }
}
