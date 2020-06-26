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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.internal.urls;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.uri.ShouldHaveScheme.shouldHaveScheme;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.net.URI;

import org.assertj.core.internal.UrisBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class Uris_assertHasScheme_Test extends UrisBaseTest {

  @ParameterizedTest
  @CsvSource({
      "http://example.com/pages/,   http",
      "example.com/pages/,          "
  })
  public void should_pass_if_actual_uri_has_the_given_scheme(URI uri, String expectedScheme) {
    // WHEN/THEN
    uris.assertHasScheme(info, uri, expectedScheme);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    URI uri = null;
    String expectedScheme = "http";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasScheme(info, uri, expectedScheme));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_scheme_is_not_the_expected_scheme() {
    // GIVEN
    URI uri = URI.create("http://example.com/pages/");
    String expectedScheme = "ftp";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasScheme(info, uri, expectedScheme));
    // THEN
    then(assertionError).hasMessage(shouldHaveScheme(uri, expectedScheme).create());
  }

}
