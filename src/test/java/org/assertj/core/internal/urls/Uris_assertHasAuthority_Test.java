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
import static org.assertj.core.error.uri.ShouldHaveAuthority.shouldHaveAuthority;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.net.URI;

import org.assertj.core.internal.UrisBaseTest;
import org.junit.jupiter.api.Test;

public class Uris_assertHasAuthority_Test extends UrisBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    URI uri = null;
    String expectedAuthority = "http://www.helloworld.org";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasAuthority(info, uri, expectedAuthority));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  public void should_pass_if_actual_uri_has_the_expected_authority() {
    // GIVEN
    URI uri = URI.create("http://www.helloworld.org:8080");
    String expectedAuthority = "www.helloworld.org:8080";
    // WHEN/THEN
    uris.assertHasAuthority(info, uri, expectedAuthority);
  }

  @Test
  public void should_pass_if_actual_uri_with_path_has_the_expected_authority() {
    // GIVEN
    URI uri = URI.create("http://www.helloworld.org:8080/pages");
    String expectedAuthority = "www.helloworld.org:8080";
    // WHEN/THEN
    uris.assertHasAuthority(info, uri, expectedAuthority);
  }

  @Test
  public void should_fail_if_actual_authority_is_not_the_expected_one_because_ports_differ() {
    // GIVEN
    URI uri = URI.create("http://example.com:8080/pages/");
    String expectedAuthority = "example.com:8888";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasAuthority(info, uri, expectedAuthority));
    // THEN
    then(assertionError).hasMessage(shouldHaveAuthority(uri, expectedAuthority).create());
  }

  @Test
  public void should_fail_if_actual_authority_is_not_the_expected_one_because_hosts_differ() {
    // GIVEN
    URI uri = URI.create("http://example.com:8080/pages/");
    String expectedAuthority = "example.org:8080";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasAuthority(info, uri, expectedAuthority));
    // THEN
    then(assertionError).hasMessage(shouldHaveAuthority(uri, expectedAuthority).create());
  }

}
