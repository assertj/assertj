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
import static org.assertj.core.error.uri.ShouldHaveAnchor.shouldHaveAnchor;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.net.MalformedURLException;
import java.net.URL;

import org.assertj.core.internal.UrlsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class Urls_assertHasAnchor_Test extends UrlsBaseTest {

  @ParameterizedTest
  @CsvSource({
      "http://www.helloworld.org/pages/index.html#print,   print",
      "http://www.helloworld.org/index.html#print,         print"
  })
  public void should_pass_if_actual_url_has_the_given_anchor(URL url, String expectedAnchor) {
    // WHEN/THEN
    urls.assertHasAnchor(info, url, expectedAnchor);
  }

  @Test
  public void should_pass_if_actual_url_has_no_anchor_and_given_is_null() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://www.helloworld.org/index.html");
    String expectedAnchor = null;
    // WHEN/THEN
    urls.assertHasAnchor(info, url, expectedAnchor);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    URL url = null;
    String expectedAnchor = "http://www.helloworld.org/index.html#print";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertHasAnchor(info, url, expectedAnchor));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_URL_has_not_the_expected_anchor() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://example.com/index.html#print");
    String expectedAnchor = "foo";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertHasAnchor(info, url, expectedAnchor));
    // THEN
    then(assertionError).hasMessage(shouldHaveAnchor(url, expectedAnchor).create());
  }

  @Test
  public void should_fail_if_actual_URL_has_no_anchor_and_expected_anchor_is_not_null() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://example.com/index.html");
    String expectedAnchor = "print";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertHasAnchor(info, url, expectedAnchor));
    // THEN
    then(assertionError).hasMessage(shouldHaveAnchor(url, expectedAnchor).create());
  }

  @Test
  public void should_fail_if_actual_URL_has_anchor_and_expected_anchor_is_null() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://example.com/index.html#print");
    String expectedAnchor = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertHasAnchor(info, url, expectedAnchor));
    // THEN
    then(assertionError).hasMessage(shouldHaveAnchor(url, expectedAnchor).create());
  }

  @Test
  public void should_throw_error_if_actual_url_has_no_anchor() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://www.helloworld.org/index.html");
    String expectedAnchor = "print";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertHasAnchor(info, url, expectedAnchor));
    // THEN
    then(assertionError).hasMessage(shouldHaveAnchor(url, expectedAnchor).create());
  }
}
