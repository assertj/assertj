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

import static org.assertj.core.error.uri.ShouldHaveScheme.shouldHaveScheme;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.net.URI;
import java.net.URISyntaxException;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.UrisBaseTest;
import org.junit.Test;

public class Uris_assertHasScheme_Test extends UrisBaseTest {

  @Test
  public void should_pass_if_actual_uri_has_the_given_scheme() throws URISyntaxException {
    uris.assertHasScheme(info, new URI("http://example.com/pages/"), "http");
    uris.assertHasScheme(info, new URI("example.com/pages/"), null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    uris.assertHasScheme(info, null, "http");
  }

  @Test
  public void should_fail_if_actual_scheme_is_not_the_expected_scheme() throws URISyntaxException {
    AssertionInfo info = someInfo();
    URI uri = new URI("http://example.com/pages/");
    String expectedScheme = "ftp";
    try {
      uris.assertHasScheme(info, uri, expectedScheme);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveScheme(uri, expectedScheme));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
