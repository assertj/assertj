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
import static org.assertj.core.error.uri.ShouldHaveUserInfo.shouldHaveUserInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.net.URI;

import org.assertj.core.internal.UrisBaseTest;
import org.junit.jupiter.api.Test;

public class Uris_assertHasUserInfo_Test extends UrisBaseTest {

  @Test
  public void should_pass_if_actual_uri_has_no_user_info_and_given_user_info_is_null() {
    // GIVEN
    URI uri = URI.create("http://www.helloworld.org/index.html");
    String expectedUserInfo = null;
    // WHEN/THEN
    uris.assertHasUserInfo(info, uri, expectedUserInfo);
  }

  @Test
  public void should_pass_if_actual_uri_has_the_expected_user_info() {
    // GIVEN
    URI uri = URI.create("http://test:pass@www.helloworld.org/index.html");
    String expectedUserInfo = "test:pass";
    // WHEN/THEN
    uris.assertHasUserInfo(info, uri, expectedUserInfo);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    URI uri = null;
    String expectedUserInfo = "http://test:pass@www.helloworld.org/index.html";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasUserInfo(info, uri, expectedUserInfo));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_URI_user_info_is_not_the_expected_user_info() {
    // GIVEN
    URI uri = URI.create("http://test:pass@assertj.org/news");
    String expectedUserInfo = "test:ok";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasUserInfo(info, uri, expectedUserInfo));
    // THEN
    then(assertionError).hasMessage(shouldHaveUserInfo(uri, expectedUserInfo).create());
  }

  @Test
  public void should_fail_if_actual_URI_has_no_user_info_and_expected_user_info_is_not_null() {
    // GIVEN
    URI uri = URI.create("http://assertj.org/news");
    String expectedUserInfo = "test:pass";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasUserInfo(info, uri, expectedUserInfo));
    // THEN
    then(assertionError).hasMessage(shouldHaveUserInfo(uri, expectedUserInfo).create());
  }

  @Test
  public void should_fail_if_actual_URI_has_a_user_info_and_expected_user_info_is_null() {
    // GIVEN
    URI uri = URI.create("http://test:pass@assertj.org");
    String expectedUserInfo = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasUserInfo(info, uri, expectedUserInfo));
    // THEN
    then(assertionError).hasMessage(shouldHaveUserInfo(uri, expectedUserInfo).create());
  }

}
