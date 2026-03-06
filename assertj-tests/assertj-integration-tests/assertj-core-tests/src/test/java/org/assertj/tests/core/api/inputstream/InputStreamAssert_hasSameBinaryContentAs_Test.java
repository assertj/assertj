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

package org.assertj.tests.core.api.inputstream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveBinaryContent.shouldHaveBinaryContent;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import org.assertj.core.internal.BinaryDiff;
import org.assertj.core.internal.BinaryDiffResult;
import org.junit.jupiter.api.Test;

/**
 * @author Jaeung Ha
 *
 */
public class InputStreamAssert_hasSameBinaryContentAs_Test {
  private static BinaryDiffResult diff(String actual, String expected) {
    try {
      return new BinaryDiff().diff(new ByteArrayInputStream(actual.getBytes()), new ByteArrayInputStream(expected.getBytes()));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    InputStream actual = null;
    InputStream expected = new ByteArrayInputStream(new byte[0]);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSameBinaryContentAs(expected));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_expected_is_null() {
    // GIVEN
    InputStream actual = new ByteArrayInputStream(new byte[0]);
    InputStream expected = null;
    // WHEN
    Exception exception = catchException(() -> assertThat(actual).hasSameBinaryContentAs(expected));
    // THEN
    then(exception).isInstanceOf(NullPointerException.class)
                   .hasMessage(shouldNotBeNull("expected").create());
  }

  @Test
  void should_rethrow_IOException() throws Exception {
    // GIVEN
    @SuppressWarnings("resource")
    InputStream actual = mock();
    InputStream expected = new ByteArrayInputStream(new byte[0]);
    IOException cause = new IOException();
    given(actual.read()).willThrow(cause);
    // WHEN
    Exception exception = catchException(() -> assertThat(actual).hasSameBinaryContentAs(expected));
    // THEN
    then(exception).isInstanceOf(UncheckedIOException.class)
                   .hasCause(cause);
  }

  @Test
  void should_pass_resetting_actual_and_expected_if_actual_has_expected_content_and_both_support_marking() {
    // GIVEN
    InputStream actual = new ByteArrayInputStream("12345".getBytes());
    InputStream expected = new ByteArrayInputStream("12345".getBytes());
    // WHEN
    assertThat(actual).hasSameBinaryContentAs(expected);
    // THEN
    then(actual).isNotEmpty();
    then(expected).isNotEmpty();
  }

  @Test
  void should_pass_resetting_actual_if_actual_has_expected_content_and_actual_supports_marking() {
    // GIVEN
    InputStream actual = new ByteArrayInputStream("12345".getBytes());
    InputStream expected = new UnmarkableByteArrayInputStream("12345".getBytes());
    // WHEN
    assertThat(actual).hasSameBinaryContentAs(expected);
    // THEN
    then(actual).isNotEmpty();
    then(expected).isEmpty();
  }

  @Test
  void should_pass_resetting_expected_if_actual_has_expected_content_and_expected_supports_marking() {
    // GIVEN
    InputStream actual = new UnmarkableByteArrayInputStream("12345".getBytes());
    InputStream expected = new ByteArrayInputStream("12345".getBytes());
    // WHEN
    assertThat(actual).hasSameBinaryContentAs(expected);
    // THEN
    then(actual).isEmpty();
    then(expected).isNotEmpty();
  }

  @Test
  void should_pass_without_resetting_if_actual_has_expected_content_and_none_supports_marking() {
    // GIVEN
    InputStream actual = new UnmarkableByteArrayInputStream("12345".getBytes());
    InputStream expected = new UnmarkableByteArrayInputStream("12345".getBytes());
    // WHEN
    assertThat(actual).hasSameBinaryContentAs(expected);
    // THEN
    then(actual).isEmpty();
    then(expected).isEmpty();
  }

  @Test
  void should_fail_resetting_actual_and_expected_if_actual_does_not_have_expected_content_and_both_support_marking() {
    // GIVEN
    InputStream actual = new ByteArrayInputStream("12345".getBytes());
    InputStream expected = new ByteArrayInputStream("67890".getBytes());
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSameBinaryContentAs(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveBinaryContent(actual, diff("12345", "67890")).create());
    then(actual).isNotEmpty();
    then(expected).isNotEmpty();
  }

  @Test
  void should_fail_resetting_actual_if_actual_does_not_have_expected_content_and_actual_supports_marking() throws IOException {
    // GIVEN
    InputStream actual = new ByteArrayInputStream("12345".getBytes());
    InputStream expected = new UnmarkableByteArrayInputStream("67890".getBytes());
    int available = expected.available();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSameBinaryContentAs(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveBinaryContent(actual, diff("12345", "67890")).create());
    then(actual).isNotEmpty();
    then(expected.available()).isLessThan(available);
    then(expected.available()).isGreaterThanOrEqualTo(0);
  }

  @Test
  void should_fail_resetting_expected_if_actual_does_not_have_expected_content_and_expected_supports_marking() throws IOException {
    // GIVEN
    InputStream actual = new UnmarkableByteArrayInputStream("12345".getBytes());
    InputStream expected = new ByteArrayInputStream("67890".getBytes());
    int available = actual.available();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSameBinaryContentAs(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveBinaryContent(actual, diff("12345", "67890")).create());
    then(expected).isNotEmpty();
    then(actual.available()).isLessThan(available);
    then(actual.available()).isGreaterThanOrEqualTo(0);
  }

  @Test
  void should_fail_without_resetting_if_actual_does_not_have_expected_content_and_none_supports_marking() throws IOException {
    // GIVEN
    InputStream actual = new UnmarkableByteArrayInputStream("12345".getBytes());
    InputStream expected = new UnmarkableByteArrayInputStream("67890".getBytes());
    int actualAvailable = actual.available();
    int expectedAvailable = expected.available();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSameBinaryContentAs(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveBinaryContent(actual, diff("12345", "67890")).create());
    then(expected.available()).isLessThan(expectedAvailable);
    then(expected.available()).isGreaterThanOrEqualTo(0);
    then(actual.available()).isLessThan(actualAvailable);
    then(actual.available()).isGreaterThanOrEqualTo(0);
  }

}
