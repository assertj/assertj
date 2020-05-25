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
import static org.assertj.core.error.uri.ShouldHaveQuery.shouldHaveQuery;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.net.MalformedURLException;
import java.net.URL;

import org.assertj.core.internal.UrlsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Urls#assertHasQuery(org.assertj.core.api.AssertionInfo, java.net.URL, String)}  </code>
 * .
 *
 * @author Alexander Bischof
 */
public class Urls_assertHasQuery_Test extends UrlsBaseTest {

  @Test
  public void should_pass_if_actual_url_has_the_expected_query() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://example.com/pages/?type=test");
    String expectedQuery = "type=test";

    // WHEN/THEN
    urls.assertHasQuery(info, url, expectedQuery);
  }

  @Test
  public void should_pass_if_actual_url_has_no_query_and_given_is_null() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://www.helloworld.org/index.html");
    String expectedQuery = null;

    // WHEN/THEN
    urls.assertHasQuery(info, url, expectedQuery);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    URL url = null;
    String expectedQuery = "http://www.helloworld.org/index.html?type=test";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertHasQuery(info, url, expectedQuery));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_URL_query_is_not_the_given_query() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://assertj.org/news?type=beta");
    String expectedQuery = "type=final";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertHasQuery(info, url, expectedQuery));
    // THEN
    then(assertionError).hasMessage(shouldHaveQuery(url, expectedQuery).create());
  }

  @Test
  public void should_fail_if_actual_URL_has_no_query_and_expected_query_is_not_null() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://assertj.org/news");
    String expectedQuery = "type=final";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertHasQuery(info, url, expectedQuery));
    // THEN
    then(assertionError).hasMessage(shouldHaveQuery(url, expectedQuery).create());
  }

  @Test
  public void should_fail_if_actual_URL_has_a_query_and_expected_query_is_null() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://assertj.org/news?type=beta");
    String expectedQuery = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertHasQuery(info, url, expectedQuery));
    // THEN
    then(assertionError).hasMessage(shouldHaveQuery(url, expectedQuery).create());
  }
}
