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

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.uri.ShouldHavePath.shouldHavePath;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.net.MalformedURLException;
import java.net.URL;

import org.assertj.core.internal.UrlsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class Urls_assertHasPath_Test extends UrlsBaseTest {

  @ParameterizedTest
  @CsvSource({
      "http://example.com/pages/,   /pages/",
      "http://example.com,          ''"
  })
  public void should_pass_if_actual_url_has_the_given_path(URL url, String expectedPath) {
    // WHEN/THEN
    urls.assertHasPath(info, url, expectedPath);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    URL url = null;
    String expectedPath = "path";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertHasPath(info, url, expectedPath));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  public void should_throw_an_exception_fail_if_given_path_is_null() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://example.com");
    String expectedPath = null;
    String erroeMessage = "Expecting given path not to be null";
    // WHEN/THEN
    assertThatIllegalArgumentException().isThrownBy(() -> urls.assertHasPath(info, url, expectedPath)).withMessage(erroeMessage);
  }

  @Test
  public void should_fail_if_actual_URL_path_is_not_the_given_path() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://example.com/pages/");
    String expectedPath = "/news/";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertHasPath(info, url, expectedPath));
    // THEN
    then(assertionError).hasMessage(shouldHavePath(url, expectedPath).create());
  }

  @Test
  public void should_fail_if_actual_URL_has_no_path_and_the_given_path_is_not_null() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://example.com");
    String expectedPath = "/news";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertHasPath(info, url, expectedPath));
    // THEN
    then(assertionError).hasMessage(shouldHavePath(url, expectedPath).create());
  }
}
