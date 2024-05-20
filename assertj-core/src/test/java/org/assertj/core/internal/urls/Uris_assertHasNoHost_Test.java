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

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.uri.ShouldHaveNoHost.shouldHaveNoHost;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Stream;

import org.assertj.core.internal.UrisBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class Uris_assertHasNoHost_Test extends UrisBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    URI actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasNoHost(info, actual));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_host_is_present() throws URISyntaxException {
    // GIVEN
    URI actual = new URI("https://example.com");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasNoHost(info, actual));
    // THEN
    then(assertionError).hasMessage(shouldHaveNoHost(actual).create());
  }

  @ParameterizedTest
  @MethodSource("uris_with_no_host")
  void should_pass_if_host_is_not_present(URI actual) {
    // WHEN/THEN
    uris.assertHasNoHost(info, actual);
  }

  private static Stream<URI> uris_with_no_host() throws URISyntaxException {
    return Stream.of(new URI("file:///etc/lsb-release"),
                     new URI("file", "", "/etc/lsb-release", null),
                     new URI("file", null, "/etc/lsb-release", null));
  }

}
