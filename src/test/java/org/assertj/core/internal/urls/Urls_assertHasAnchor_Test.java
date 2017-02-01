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

import static org.assertj.core.error.uri.ShouldHaveAnchor.shouldHaveAnchor;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.net.MalformedURLException;
import java.net.URL;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.UrlsBaseTest;
import org.junit.Test;

public class Urls_assertHasAnchor_Test extends UrlsBaseTest {

  @Test
  public void should_pass_if_actual_url_has_the_given_anchor() throws MalformedURLException {
    urls.assertHasAnchor(info, new URL("http://www.helloworld.org/pages/index.html#print"), "print");
    urls.assertHasAnchor(info, new URL("http://www.helloworld.org/index.html#print"), "print");
  }

  @Test
  public void should_pass_if_actual_url_has_no_anchor_and_given_is_null() throws MalformedURLException {
    urls.assertHasAnchor(info, new URL("http://www.helloworld.org/index.html"), null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    urls.assertHasAnchor(info, null, "http://www.helloworld.org/index.html#print");
  }

  @Test
  public void should_fail_if_actual_URL_has_not_the_expected_anchor() throws MalformedURLException {
    AssertionInfo info = someInfo();
    URL url = new URL("http://example.com/index.html#print");
    String expectedAnchor = "foo";
    try {
      urls.assertHasAnchor(info, url, expectedAnchor);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveAnchor(url, expectedAnchor));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_URL_has_no_anchor_and_expected_anchor_is_not_null() throws MalformedURLException {
    AssertionInfo info = someInfo();
    URL url = new URL("http://example.com/index.html");
    String expectedAnchor = "print";
    try {
      urls.assertHasAnchor(info, url, expectedAnchor);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveAnchor(url, expectedAnchor));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_URL_has_anchor_and_expected_anchor_is_null() throws MalformedURLException {
    AssertionInfo info = someInfo();
    URL url = new URL("http://example.com/index.html#print");
    String expectedAnchor = null;
    try {
      urls.assertHasAnchor(info, url, expectedAnchor);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveAnchor(url, expectedAnchor));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_throw_error_if_actual_url_has_no_anchor() throws MalformedURLException {
    thrown.expectNullPointerException(null);
    urls.assertHasAnchor(info, new URL("http://www.helloworld.org/index.html"), "print");
  }
}
