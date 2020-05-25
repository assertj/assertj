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
import static org.assertj.core.error.uri.ShouldHaveProtocol.shouldHaveProtocol;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.net.MalformedURLException;
import java.net.URL;

import org.assertj.core.internal.UrlsBaseTest;
import org.junit.jupiter.api.Test;

public class Urls_assertHasProtocol_Test extends UrlsBaseTest {

  @Test
  public void should_pass_if_actual_uri_has_the_given_protocol() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://example.com/pages/");
    String expectedProtocol = "http";

    // WHEN/THEN
    urls.assertHasProtocol(info, url, expectedProtocol);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    URL url = null;
    String expectedProtocol = "http";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertHasProtocol(info, url, expectedProtocol));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_protocol_is_not_the_expected_protocol() throws MalformedURLException {
    // GIVEN
    URL url = new URL("http://example.com/pages/");
    String expectedProtocol = "ftp";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> urls.assertHasProtocol(info, url, expectedProtocol));
    // THEN
    then(assertionError).hasMessage(shouldHaveProtocol(url, expectedProtocol).create());
  }

}
