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
import static org.assertj.core.error.uri.ShouldHavePath.shouldHavePath;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.net.URI;

import org.assertj.core.internal.UrisBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class Uris_assertHasPath_Test extends UrisBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    URI uri = null;
    String expectedPath = "path";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasPath(info, uri, expectedPath));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @ParameterizedTest
  @CsvSource({
    "http://example.com/pages/,   /pages/",
    "http://example.com,          ''"
  })
  public void should_pass_if_actual_uri_has_the_given_path(URI uri,  String expectedPath) {
    // WHEN/THEN
    uris.assertHasPath(info, uri, expectedPath);
  }

  @Test
  public void should_pass_if_actual_uri_has_no_path_and_the_given_path_is_null() {
    // GIVEN
    URI uri = URI.create("mailto:java-net@java.sun.com");
    String expectedPath = null;

    // WHEN/THEN
    uris.assertHasPath(info, uri, expectedPath);
  }

  @Test
  public void should_fail_if_actual_URI_path_is_not_the_given_path() {
    // GIVEN
    URI uri = URI.create("http://example.com/pages/");
    String expectedPath = "/news/";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasPath(info, uri, expectedPath));
    // THEN
    then(assertionError).hasMessage(shouldHavePath(uri, expectedPath).create());
  }

  @Test
  public void should_fail_if_actual_URI_has_path_and_the_given_path_null() {
    // GIVEN
    URI uri = URI.create("http://example.com/pages/");
    String expectedPath = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasPath(info, uri, expectedPath));
    // THEN
    then(assertionError).hasMessage(shouldHavePath(uri, expectedPath).create());
  }

  @Test
  public void should_fail_if_actual_URI_has_no_path_and_the_given_path_is_not_null() {
    // GIVEN
    URI uri = URI.create("mailto:java-net@java.sun.com");
    String expectedPath = "";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> uris.assertHasPath(info, uri, expectedPath));
    // THEN
    then(assertionError).hasMessage(shouldHavePath(uri, expectedPath).create());
  }
}
