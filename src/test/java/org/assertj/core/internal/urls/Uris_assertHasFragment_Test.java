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
import static org.assertj.core.error.uri.ShouldHaveFragment.shouldHaveFragment;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.net.URI;

import org.assertj.core.internal.UrisBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Uris#assertHasFragment(org.assertj.core.api.AssertionInfo, java.net.URI, String)}  </code>
 * .
 *
 * @author Alexander Bischof
 */
public class Uris_assertHasFragment_Test extends UrisBaseTest {

  @ParameterizedTest
  @CsvSource({
      "http://www.helloworld.org/pages/index.html#print,     print",
      "http://www.helloworld.org/index.html#print,   print"
  })
  public void should_pass_if_actual_uri_has_the_given_fragment(URI uri, String expectedFragment) {
    // WHEN/THEN
    uris.assertHasFragment(info, uri, expectedFragment);
  }

  @Test
  public void should_pass_if_actual_uri_has_no_fragment_and_given_is_null() {
    // GIVEN
    URI uri = URI.create("http://www.helloworld.org/index.html");
    // WHEN/THEN
    uris.assertHasFragment(info, uri, null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    URI uri = null;
    String expectedFragment = "http://www.helloworld.org/index.html#print";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasFragment(info, uri, expectedFragment));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_URI_has_not_the_expected_fragment() {
    // GIVEN
    URI uri = URI.create("http://example.com/index.html#print");
    String expectedFragment = "foo";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasFragment(info, uri, expectedFragment));
    // THEN
    then(assertionError).hasMessage(shouldHaveFragment(uri, expectedFragment).create());
  }

  @Test
  public void should_fail_if_actual_URI_has_no_fragment_and_expected_fragment_is_not_null() {
    // GIVEN
    URI uri = URI.create("http://example.com/index.html");
    String expectedFragment = "print";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasFragment(info, uri, expectedFragment));
    // THEN
    then(assertionError).hasMessage(shouldHaveFragment(uri, expectedFragment).create());
  }

  @Test
  public void should_fail_if_actual_URI_has_fragment_and_expected_fragment_is_null() {
    // GIVEN
    URI uri = URI.create("http://example.com/index.html#print");
    String expectedFragment = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasFragment(info, uri, expectedFragment));
    // THEN
    then(assertionError).hasMessage(shouldHaveFragment(uri, expectedFragment).create());
  }

  @Test
  public void should_throw_error_if_actual_uri_has_no_fragment() {
    // GIVEN
    URI uri = URI.create("http://www.helloworld.org/index.html");
    String expectedFragment = "print";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasFragment(info, uri, expectedFragment));
    // THEN
    then(assertionError).hasMessage(shouldHaveFragment(uri, expectedFragment).create());
  }
}
