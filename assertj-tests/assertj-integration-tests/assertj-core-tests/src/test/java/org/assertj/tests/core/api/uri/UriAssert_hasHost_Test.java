/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.uri;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.uri.ShouldHaveHost.shouldHaveHost;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class UriAssert_hasHost_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    URI uri = null;
    String expectedHost = "helloworld.org";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(uri).hasHost(expectedHost));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @ParameterizedTest
  @CsvSource({
      "http://helloworld.org/pages,      helloworld.org",
      "http://helloworld.org:8080,       helloworld.org",
      "http://www.helloworld.org,        www.helloworld.org",
      "http://www.helloworld.org:8080,   www.helloworld.org"
  })
  void should_pass_if_actual_uri_has_the_given_host(URI uri, String expectedHost) {
    // WHEN/THEN
    assertThat(uri).hasHost(expectedHost);
  }

  @Test
  void should_throw_NullPointerException_if_expected_host_is_null() {
    // GIVEN
    URI uri = URI.create("http://www.helloworld.org");
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(uri).hasHost(null));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage("The expected host should not be null");
  }

  @Test
  void should_fail_if_actual_URI_host_is_not_the_given_host() {
    // GIVEN
    URI uri = URI.create("http://helloworld.org");
    String expectedHost = "other.org";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(uri).hasHost(expectedHost));
    // THEN
    then(assertionError).hasMessage(shouldHaveHost(uri, expectedHost).create());
  }

  @Test
  void should_fail_if_actual_URI_has_no_host_and_the_given_host_is_not_null() throws URISyntaxException {
    // GIVEN
    URI uri = new URI("file", null, "/home/user/Documents/hello-world.txt", null);
    String expectedHost = "helloworld.org";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(uri).hasHost(expectedHost));
    // THEN
    then(assertionError).hasMessage(shouldHaveHost(uri, expectedHost).create());
  }

}
