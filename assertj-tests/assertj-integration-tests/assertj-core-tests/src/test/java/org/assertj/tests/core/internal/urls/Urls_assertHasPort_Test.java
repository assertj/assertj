/*
 * Copyright 2012-2025 the original author or authors.
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
package org.assertj.tests.core.internal.urls;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.uri.ShouldHavePort.shouldHavePort;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.Test;

class Urls_assertHasPort_Test extends UrlsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    URL url = null;
    int expectedPort = 8080;
    // WHEN
    var assertionError = expectAssertionError(() -> urls.assertHasPort(info, url, expectedPort));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_url_has_the_given_port() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://example.com:8080/pages/");
    int expectedPort = 8080;

    // WHEN/THEN
    urls.assertHasPort(info, url, expectedPort);
  }

  @Test
  void should_fail_if_actual_URL_port_is_not_the_given_port() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://example.com:8080/pages/");
    int expectedPort = 8888;
    // WHEN
    var assertionError = expectAssertionError(() -> urls.assertHasPort(info, url, expectedPort));
    // THEN
    then(assertionError).hasMessage(shouldHavePort(url, expectedPort).create());
  }

}
