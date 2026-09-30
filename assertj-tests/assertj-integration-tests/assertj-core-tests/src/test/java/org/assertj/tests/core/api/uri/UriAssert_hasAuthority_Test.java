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
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.uri.ShouldHaveAuthority.shouldHaveAuthority;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class UriAssert_hasAuthority_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    URI uri = null;
    String expectedAuthority = "helloworld.org:8080";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(uri).hasAuthority(expectedAuthority));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @ParameterizedTest
  @CsvSource({
      "http://helloworld.org:8080,               helloworld.org:8080",
      "http://www.helloworld.org:8080/news,      www.helloworld.org:8080"
  })
  void should_pass_if_actual_uri_has_the_given_authority(URI uri, String expectedAuthority) {
    // WHEN/THEN
    assertThat(uri).hasAuthority(expectedAuthority);
  }

  @Test
  void should_fail_if_actual_URI_authority_is_not_the_given_authority() {
    // GIVEN
    URI uri = URI.create("http://www.helloworld.org:8080");
    String expectedAuthority = "www.helloworld.org";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(uri).hasAuthority(expectedAuthority));
    // THEN
    then(assertionError).hasMessage(shouldHaveAuthority(uri, expectedAuthority).create());
  }

}
