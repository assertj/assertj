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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.tests.core.api.inputstream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveSameContent.shouldHaveSameContent;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;
import org.assertj.core.internal.Diff;
import org.assertj.core.util.diff.Delta;
import org.junit.jupiter.api.Test;

/**
 * @author Stephan Windmüller
 */
class InputStreamAssert_hasContent_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    InputStream actual = null;
    String expected = "";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasContent(expected));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_expected_is_null() {
    // GIVEN
    InputStream actual = new ByteArrayInputStream(new byte[0]);
    String expected = null;
    // WHEN
    Exception exception = catchException(() -> assertThat(actual).hasContent(expected));
    // THEN
    then(exception).isInstanceOf(NullPointerException.class)
                   .hasMessage(shouldNotBeNull("expected").create());
  }

  @Test
  void should_rethrow_IOException() throws Exception {
    // GIVEN
    @SuppressWarnings("resource")
    InputStream actual = mock();
    String expected = "";
    IOException cause = new IOException();
    given(actual.read(any(), anyInt(), anyInt())).willThrow(cause);
    // WHEN
    Exception exception = catchException(() -> assertThat(actual).hasContent(expected));
    // THEN
    then(exception).isInstanceOf(UncheckedIOException.class)
                   .hasCause(cause);
  }

  @Test
  void should_pass_resetting_actual_if_actual_has_expected_content_and_supports_marking() {
    // GIVEN
    InputStream actual = new ByteArrayInputStream("12345".getBytes());
    String expected = "12345";
    // WHEN
    assertThat(actual).hasContent(expected);
    // THEN
    then(actual).isNotEmpty();
  }

  @Test
  void should_pass_without_resetting_if_actual_has_expected_content_and_does_not_supports_marking() {
    // GIVEN
    InputStream actual = new UnmarkableByteArrayInputStream("12345".getBytes());
    String expected = "12345";
    // WHEN
    assertThat(actual).hasContent(expected);
    // THEN
    then(actual).isEmpty();
  }

  @Test
  void should_fail_resetting_actual_if_actual_does_not_have_expected_content_and_supports_marking() {
    // GIVEN
    InputStream actual = new ByteArrayInputStream("12345".getBytes());
    String expected = "67890";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasContent(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveSameContent(actual, expected, diff("12345", "67890")).create());
    then(actual).isNotEmpty();
  }

  @Test
  void should_fail_without_resetting_if_actual_does_not_have_expected_content_and_does_not_support_marking() {
    // GIVEN
    InputStream actual = new UnmarkableByteArrayInputStream("12345".getBytes());
    String expected = "67890";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasContent(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveSameContent(actual, expected, diff("12345", "67890")).create());
    then(actual).isEmpty();
  }

  private static List<Delta<String>> diff(String actual, String expected) {
    try {
      return new Diff().diff(new ByteArrayInputStream(actual.getBytes()), expected);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

}
