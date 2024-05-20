/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.urls;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.uri.ShouldHaveHost.shouldHaveHost;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.net.URI;

import org.assertj.core.internal.UrisBaseTest;
import org.junit.jupiter.api.Test;

class Uris_assertHasHost_Test extends UrisBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    URI actual = null;
    String expected = "www.helloworld.org";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasHost(info, actual, expected));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_is_null() {
    // GIVEN
    URI actual = URI.create("http://www.helloworld.org");
    String expected = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> uris.assertHasHost(info, actual, expected));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage("The expected host should not be null");
  }

  @Test
  void should_pass_if_actual_URI_has_the_given_host() {
    // GIVEN
    URI actual = URI.create("http://www.helloworld.org");
    String expected = "www.helloworld.org";
    // WHEN/THEN
    uris.assertHasHost(info, actual, expected);
  }

  @Test
  void should_pass_if_actual_URI_with_path_has_the_given_host() {
    // GIVEN
    URI actual = URI.create("http://www.helloworld.org/pages");
    String expected = "www.helloworld.org";
    // WHEN/THEN
    uris.assertHasHost(info, actual, expected);
  }

  @Test
  void should_fail_if_actual_URI_has_not_the_expected_host() {
    // GIVEN
    URI actual = URI.create("http://example.com/pages/");
    String expected = "example.org";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasHost(info, actual, expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveHost(actual, expected).create());
  }

}
