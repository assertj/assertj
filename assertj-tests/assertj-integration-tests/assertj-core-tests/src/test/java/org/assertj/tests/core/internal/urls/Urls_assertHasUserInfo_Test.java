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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.core.internal.urls;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.uri.ShouldHaveUserInfo.shouldHaveUserInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.Test;

class Urls_assertHasUserInfo_Test extends UrlsBaseTest {

  @Test
  void should_pass_if_actual_url_has_no_user_info_and_given_user_info_is_null() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://www.helloworld.org/index.html");
    String expectedUserInfo = null;

    // WHEN/THEN
    urls.assertHasUserInfo(info, url, expectedUserInfo);
  }

  @Test
  void should_pass_if_actual_url_has_the_expected_user_info() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://test:pass@www.helloworld.org/index.html");
    String expectedUserInfo = "test:pass";

    // WHEN/THEN
    urls.assertHasUserInfo(info, url, expectedUserInfo);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    URL url = null;
    String expectedUserInfo = "http://test:pass@www.helloworld.org/index.html";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertHasUserInfo(info, url, expectedUserInfo));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_URL_user_info_is_not_the_expected_user_info() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://test:pass@assertj.org/news");
    String expectedUserInfo = "test:ok";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertHasUserInfo(info, url, expectedUserInfo));
    // THEN
    then(assertionError).hasMessage(shouldHaveUserInfo(url, expectedUserInfo).create());
  }

  @Test
  void should_fail_if_actual_URL_has_no_user_info_and_expected_user_info_is_not_null()
                                                                                       throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://assertj.org/news");
    String expectedUserInfo = "test:pass";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertHasUserInfo(info, url, expectedUserInfo));
    // THEN
    then(assertionError).hasMessage(shouldHaveUserInfo(url, expectedUserInfo).create());
  }

  @Test
  void should_fail_if_actual_URL_has_a_user_info_and_expected_user_info_is_null() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://test:pass@assertj.org");
    String expectedUserInfo = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertHasUserInfo(info, url, expectedUserInfo));
    // THEN
    then(assertionError).hasMessage(shouldHaveUserInfo(url, expectedUserInfo).create());
  }

}
