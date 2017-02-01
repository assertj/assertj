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

import static org.assertj.core.error.uri.ShouldHaveHost.shouldHaveHost;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.net.URI;
import java.net.URISyntaxException;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.UrisBaseTest;
import org.junit.Test;

public class Uris_assertHasHost_Test extends UrisBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    uris.assertHasHost(info, null, "www.helloworld.org");
  }

  @Test
  public void should_pass_if_actual_URI_has_the_given_host() throws URISyntaxException {
    uris.assertHasHost(info, new URI("http://www.helloworld.org"), "www.helloworld.org");
  }

  @Test
  public void should_pass_if_actual_URI_with_path_has_the_given_host() throws URISyntaxException {
    uris.assertHasHost(info, new URI("http://www.helloworld.org/pages"), "www.helloworld.org");
  }

  @Test
  public void should_fail_if_actual_URI_has_not_the_expected_host() throws URISyntaxException {
    AssertionInfo info = someInfo();
    URI uri = new URI("http://example.com/pages/");
    String expectedHost = "example.org";
    try {
      uris.assertHasHost(info, uri, expectedHost);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveHost(uri, expectedHost));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
