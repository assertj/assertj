/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal.urls;

import static org.assertj.core.error.uri.ShouldHaveUserInfo.shouldHaveUserInfo;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.net.MalformedURLException;
import java.net.URL;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.UrlsBaseTest;
import org.junit.Test;

public class Urls_assertHasUserInfo_Test extends UrlsBaseTest {

  @Test
  public void should_pass_if_actual_url_has_no_user_info_and_given_user_info_is_null() throws MalformedURLException {
    urls.assertHasUserInfo(info, new URL("http://www.helloworld.org/index.html"), null);
  }

  @Test
  public void should_pass_if_actual_url_has_the_expected_user_info() throws MalformedURLException {
    urls.assertHasUserInfo(info, new URL("http://test:pass@www.helloworld.org/index.html"), "test:pass");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    urls.assertHasUserInfo(info, null, "http://test:pass@www.helloworld.org/index.html");
  }

  @Test
  public void should_fail_if_actual_URL_user_info_is_not_the_expected_user_info() throws MalformedURLException {
    AssertionInfo info = someInfo();
    URL url = new URL("http://test:pass@assertj.org/news");
    String expectedUserInfo = "test:ok";
    try {
      urls.assertHasUserInfo(info, url, expectedUserInfo);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveUserInfo(url, expectedUserInfo));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_URL_has_no_user_info_and_expected_user_info_is_not_null()
      throws MalformedURLException {
    AssertionInfo info = someInfo();
    URL url = new URL("http://assertj.org/news");
    String expectedUserInfo = "test:pass";
    try {
      urls.assertHasUserInfo(info, url, expectedUserInfo);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveUserInfo(url, expectedUserInfo));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_URL_has_a_user_info_and_expected_user_info_is_null() throws MalformedURLException {
    AssertionInfo info = someInfo();
    URL url = new URL("http://test:pass@assertj.org");
    String expectedUserInfo = null;
    try {
      urls.assertHasUserInfo(info, url, expectedUserInfo);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveUserInfo(url, expectedUserInfo));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
