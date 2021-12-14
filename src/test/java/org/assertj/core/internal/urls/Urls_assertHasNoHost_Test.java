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

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.uri.ShouldHaveNoHost.shouldHaveNoHost;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Stream;

import org.assertj.core.internal.UrlsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class Urls_assertHasNoHost_Test extends UrlsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    URL actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertHasNoHost(info, actual));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_host_present() throws MalformedURLException {
    // GIVEN
    URL actual = new URL("https://example.com");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertHasNoHost(info, actual));
    // THEN
    then(assertionError).hasMessage(shouldHaveNoHost(actual).create());
  }

  @ParameterizedTest
  @MethodSource("urls_with_no_host")
  void should_pass_if_host_is_not_present(URL actual) {
    // WHEN/THEN
    urls.assertHasNoHost(info, actual);
  }

  private static Stream<URL> urls_with_no_host() throws MalformedURLException {
    return Stream.of(new URL("file:///etc/lsb-release"),
                     new URL("file", "", "/etc/lsb-release"),
                     new URL("file", null, "/etc/lsb-release"));
  }

}
