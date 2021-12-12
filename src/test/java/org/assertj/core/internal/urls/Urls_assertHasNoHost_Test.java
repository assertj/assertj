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

import java.net.MalformedURLException;
import java.net.URL;
import org.assertj.core.internal.UrlsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.uri.ShouldHaveNoHost.shouldHaveNoHost;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

class Urls_assertHasNoHost_Test extends UrlsBaseTest {

  @Test
  void should_fail_if_host_present() throws MalformedURLException {
    // GIVEN
    URL url = new URL("https://example.com");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertHasNoHost(info, url));
    // THEN
    then(assertionError).hasMessage(shouldHaveNoHost(url).create());
  }

  @Test
  void should_pass_if_no_host_present() throws MalformedURLException {
    // GIVEN
    URL url = new URL("file:///etc/lsb-release");
    // WHEN/THEN
    urls.assertHasNoHost(info, url);
  }

  @ParameterizedTest
  @NullAndEmptySource
  void should_pass_if_host_is_explicitly_missing(String host) throws MalformedURLException {
    // GIVEN
    URL url = new URL("file", host, "/etc/lsb-release");
    // WHEN/THEN
    urls.assertHasNoHost(info, url);
  }
}
