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
import static org.assertj.core.error.uri.ShouldHaveFragment.shouldHaveFragment;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.net.URI;

import org.junit.jupiter.api.Test;

class UriAssert_hasFragment_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    URI uri = null;
    String expectedFragment = "print";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(uri).hasFragment(expectedFragment));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_uri_has_the_given_fragment() {
    // GIVEN
    URI uri = URI.create("http://helloworld.org:8080/index.html#print");
    String expectedFragment = "print";
    // WHEN/THEN
    assertThat(uri).hasFragment(expectedFragment);
  }

  @Test
  void should_fail_if_actual_URI_fragment_is_not_the_given_fragment() {
    // GIVEN
    URI uri = URI.create("http://helloworld.org:8080/index.html#print");
    String expectedFragment = "hello";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(uri).hasFragment(expectedFragment));
    // THEN
    then(assertionError).hasMessage(shouldHaveFragment(uri, expectedFragment).create());
  }

  @Test
  void should_fail_if_actual_URI_has_no_fragment_and_the_given_fragment_is_not_null() {
    // GIVEN
    URI uri = URI.create("http://helloworld.org:8080/index.html");
    String expectedFragment = "hello";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(uri).hasFragment(expectedFragment));
    // THEN
    then(assertionError).hasMessage(shouldHaveFragment(uri, expectedFragment).create());
  }

}
