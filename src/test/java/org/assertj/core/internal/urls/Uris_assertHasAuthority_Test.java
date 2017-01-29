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

import static org.assertj.core.error.uri.ShouldHaveAuthority.shouldHaveAuthority;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.net.URI;
import java.net.URISyntaxException;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.UrisBaseTest;
import org.junit.Test;

public class Uris_assertHasAuthority_Test extends UrisBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    uris.assertHasAuthority(info, null, "http://www.helloworld.org");
  }

  @Test
  public void should_pass_if_actual_uri_has_the_expected_authority() throws URISyntaxException {
    uris.assertHasAuthority(info, new URI("http://www.helloworld.org:8080"), "www.helloworld.org:8080");
  }

  @Test
  public void should_pass_if_actual_uri_with_path_has_the_expected_authority() throws URISyntaxException {
    uris.assertHasAuthority(info, new URI("http://www.helloworld.org:8080/pages"), "www.helloworld.org:8080");
  }

  @Test
  public void should_fail_if_actual_authority_is_not_the_expected_one_because_ports_differ() throws URISyntaxException {
    AssertionInfo info = someInfo();
    URI uri = new URI("http://example.com:8080/pages/");
    String expectedAuthority = "example.com:8888";
    try {
      uris.assertHasAuthority(info, uri, expectedAuthority);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveAuthority(uri, expectedAuthority));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_authority_is_not_the_expected_one_because_hosts_differ() throws URISyntaxException {
    AssertionInfo info = someInfo();
    URI uri = new URI("http://example.com:8080/pages/");
    String expectedAuthority = "example.org:8080";
    try {
      uris.assertHasAuthority(info, uri, expectedAuthority);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveAuthority(uri, expectedAuthority));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
