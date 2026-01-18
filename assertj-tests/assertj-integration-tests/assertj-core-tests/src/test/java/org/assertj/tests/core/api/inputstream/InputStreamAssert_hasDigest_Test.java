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

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.security.MessageDigest.getInstance;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveDigest.shouldHaveDigest;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.internal.Digests.toHex;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.assertj.core.internal.DigestDiff;
import org.assertj.tests.core.testkit.junit.jupiter.params.converter.Hex;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * @author Valeriy Vyrva
 */
class InputStreamAssert_hasDigest_Test {

  @Nested
  class With_MessageDigest_and_byte_array {

    @Test
    void should_fail_if_actual_is_null() throws Exception {
      // GIVEN
      InputStream actual = null;
      MessageDigest algorithm = getInstance("MD5");
      byte[] digest = new byte[16];
      // WHEN
      var assertionError = expectAssertionError(() -> assertThat(actual).hasDigest(algorithm, digest));
      // THEN
      then(assertionError).hasMessage(shouldNotBeNull().create());
    }

    @Test
    void should_fail_if_algorithm_is_null() {
      // GIVEN
      InputStream actual = new ByteArrayInputStream(new byte[0]);
      MessageDigest algorithm = null;
      byte[] digest = new byte[16];
      // WHEN
      Exception exception = catchException(() -> assertThat(actual).hasDigest(algorithm, digest));
      // THEN
      then(exception).isInstanceOf(NullPointerException.class)
                     .hasMessage(shouldNotBeNull("algorithm").create());
    }

    @Test
    void should_fail_if_digest_is_null() throws Exception {
      // GIVEN
      InputStream actual = new ByteArrayInputStream(new byte[0]);
      MessageDigest algorithm = getInstance("MD5");
      byte[] digest = null;
      // WHEN
      Exception exception = catchException(() -> assertThat(actual).hasDigest(algorithm, digest));
      // THEN
      then(exception).isInstanceOf(NullPointerException.class)
                     .hasMessage(shouldNotBeNull("digest").create());
    }

    @Test
    void should_rethrow_IOException() throws Exception {
      // GIVEN
      @SuppressWarnings("resource")
      InputStream actual = mock();
      MessageDigest algorithm = getInstance("MD5");
      byte[] digest = new byte[16];
      IOException cause = new IOException();
      given(actual.read(any())).willThrow(cause);
      // WHEN
      Exception exception = catchException(() -> assertThat(actual).hasDigest(algorithm, digest));
      // THEN
      then(exception).isInstanceOf(UncheckedIOException.class)
                     .hasCause(cause);
    }

    @ParameterizedTest
    @Parameters
    void should_pass_resetting_actual_if_actual_has_expected_digest_and_supports_marking(@Hex byte[] input,
                                                                                         MessageDigest algorithm,
                                                                                         @Hex byte[] expected) {
      // GIVEN
      InputStream actual = new ByteArrayInputStream(input);
      // WHEN
      assertThat(actual).hasDigest(algorithm, expected);
      // THEN
      then(actual).isNotEmpty();
    }

    @ParameterizedTest
    @Parameters
    void should_pass_without_resetting_actual_if_actual_has_expected_digest_and_does_not_support_marking(@Hex byte[] input,
                                                                                                         MessageDigest algorithm,
                                                                                                         @Hex byte[] expected) {
      // GIVEN
      InputStream actual = new UnmarkableByteArrayInputStream(input);
      // WHEN
      assertThat(actual).hasDigest(algorithm, expected);
      // THEN
      then(actual).isEmpty();
    }

    @ParameterizedTest
    @Parameters
    void should_fail_if_actual_does_not_have_expected_digest(@Hex byte[] input, MessageDigest algorithm, String digest) {
      // GIVEN
      InputStream actual = new ByteArrayInputStream(input);
      byte[] expected = new byte[16];
      // WHEN
      var assertionError = expectAssertionError(() -> assertThat(actual).hasDigest(algorithm, expected));
      // THEN
      then(assertionError).hasMessage(shouldHaveDigest(actual, new DigestDiff(digest, toHex(expected), algorithm)).create());
    }

  }

  @Nested
  class With_MessageDigest_and_String {

    @Test
    void should_fail_if_actual_is_null() throws Exception {
      // GIVEN
      InputStream actual = null;
      MessageDigest algorithm = getInstance("MD5");
      String digest = "00000000000000000000000000000000";
      // WHEN
      var assertionError = expectAssertionError(() -> assertThat(actual).hasDigest(algorithm, digest));
      // THEN
      then(assertionError).hasMessage(shouldNotBeNull().create());
    }

    @Test
    void should_fail_if_algorithm_is_null() {
      // GIVEN
      InputStream actual = new ByteArrayInputStream(new byte[0]);
      MessageDigest algorithm = null;
      String digest = "00000000000000000000000000000000";
      // WHEN
      Exception exception = catchException(() -> assertThat(actual).hasDigest(algorithm, digest));
      // THEN
      then(exception).isInstanceOf(NullPointerException.class)
                     .hasMessage(shouldNotBeNull("algorithm").create());
    }

    @Test
    void should_fail_if_digest_is_null() throws Exception {
      // GIVEN
      InputStream actual = new ByteArrayInputStream(new byte[0]);
      MessageDigest algorithm = getInstance("MD5");
      String digest = null;
      // WHEN
      Exception exception = catchException(() -> assertThat(actual).hasDigest(algorithm, digest));
      // THEN
      then(exception).isInstanceOf(NullPointerException.class)
                     .hasMessage(shouldNotBeNull("digest").create());
    }

    @Test
    void should_rethrow_IOException() throws Exception {
      // GIVEN
      @SuppressWarnings("resource")
      InputStream actual = mock();
      MessageDigest algorithm = getInstance("MD5");
      String digest = "00000000000000000000000000000000";
      IOException cause = new IOException();
      given(actual.read(any())).willThrow(cause);
      // WHEN
      Exception exception = catchException(() -> assertThat(actual).hasDigest(algorithm, digest));
      // THEN
      then(exception).isInstanceOf(UncheckedIOException.class)
                     .hasCause(cause);
    }

    @ParameterizedTest
    @Parameters
    void should_pass_resetting_actual_if_actual_has_expected_digest_and_supports_marking(@Hex byte[] input,
                                                                                         MessageDigest algorithm,
                                                                                         String expected) {
      // GIVEN
      InputStream actual = new ByteArrayInputStream(input);
      // WHEN
      assertThat(actual).hasDigest(algorithm, expected);
      // THEN
      then(actual).isNotEmpty();
    }

    @ParameterizedTest
    @Parameters
    void should_pass_without_resetting_actual_if_actual_has_expected_digest_and_does_not_support_marking(@Hex byte[] input,
                                                                                                         MessageDigest algorithm,
                                                                                                         String expected) {
      // GIVEN
      InputStream actual = new UnmarkableByteArrayInputStream(input);
      // WHEN
      assertThat(actual).hasDigest(algorithm, expected);
      // THEN
      then(actual).isEmpty();
    }

    @ParameterizedTest
    @Parameters
    void should_fail_if_actual_does_not_have_expected_digest(@Hex byte[] input, MessageDigest algorithm, String digest) {
      // GIVEN
      InputStream actual = new ByteArrayInputStream(input);
      String expected = "00000000000000000000000000000000";
      // WHEN
      var assertionError = expectAssertionError(() -> assertThat(actual).hasDigest(algorithm, expected));
      // THEN
      then(assertionError).hasMessage(shouldHaveDigest(actual, new DigestDiff(digest, expected, algorithm)).create());
    }

  }

  @Nested
  class With_String_and_byte_array {

    @Test
    void should_fail_if_actual_is_null() {
      // GIVEN
      InputStream actual = null;
      String algorithm = "MD5";
      byte[] digest = new byte[16];
      // WHEN
      var assertionError = expectAssertionError(() -> assertThat(actual).hasDigest(algorithm, digest));
      // THEN
      then(assertionError).hasMessage(shouldNotBeNull().create());
    }

    @Test
    void should_fail_if_algorithm_is_null() {
      // GIVEN
      InputStream actual = new ByteArrayInputStream(new byte[0]);
      String algorithm = null;
      byte[] digest = new byte[16];
      // WHEN
      Exception exception = catchException(() -> assertThat(actual).hasDigest(algorithm, digest));
      // THEN
      then(exception).isInstanceOf(NullPointerException.class)
                     .hasMessage(shouldNotBeNull("algorithm").create());
    }

    @Test
    void should_fail_if_algorithm_is_invalid() {
      // GIVEN
      InputStream actual = new ByteArrayInputStream(new byte[0]);
      String algorithm = "non-existing";
      byte[] digest = new byte[16];
      // WHEN
      Exception exception = catchException(() -> assertThat(actual).hasDigest(algorithm, digest));
      // THEN
      then(exception).isInstanceOf(IllegalArgumentException.class)
                     .cause().isInstanceOf(NoSuchAlgorithmException.class);
    }

    @Test
    void should_fail_if_digest_is_null() {
      // GIVEN
      InputStream actual = new ByteArrayInputStream(new byte[0]);
      String algorithm = "MD5";
      byte[] digest = null;
      // WHEN
      Exception exception = catchException(() -> assertThat(actual).hasDigest(algorithm, digest));
      // THEN
      then(exception).isInstanceOf(NullPointerException.class)
                     .hasMessage(shouldNotBeNull("digest").create());
    }

    @Test
    void should_rethrow_IOException() throws Exception {
      // GIVEN
      @SuppressWarnings("resource")
      InputStream actual = mock();
      String algorithm = "MD5";
      byte[] digest = new byte[16];
      IOException cause = new IOException();
      given(actual.read(any())).willThrow(cause);
      // WHEN
      Exception exception = catchException(() -> assertThat(actual).hasDigest(algorithm, digest));
      // THEN
      then(exception).isInstanceOf(UncheckedIOException.class)
                     .hasCause(cause);
    }

    @ParameterizedTest
    @Parameters
    void should_pass_resetting_actual_if_actual_has_expected_digest_and_supports_marking(@Hex byte[] input, String algorithm,
                                                                                         @Hex byte[] expected) {
      // GIVEN
      InputStream actual = new ByteArrayInputStream(input);
      // WHEN
      assertThat(actual).hasDigest(algorithm, expected);
      // THEN
      then(actual).isNotEmpty();
    }

    @ParameterizedTest
    @Parameters
    void should_pass_without_resetting_actual_if_actual_has_expected_digest_and_does_not_support_marking(@Hex byte[] input,
                                                                                                         String algorithm,
                                                                                                         @Hex byte[] expected) {
      // GIVEN
      InputStream actual = new UnmarkableByteArrayInputStream(input);
      // WHEN
      assertThat(actual).hasDigest(algorithm, expected);
      // THEN
      then(actual).isEmpty();
    }

    @ParameterizedTest
    @Parameters
    void should_fail_if_actual_does_not_have_expected_digest(@Hex byte[] input, String algorithm,
                                                             String digest) throws Exception {
      // GIVEN
      InputStream actual = new ByteArrayInputStream(input);
      byte[] expected = new byte[16];
      // WHEN
      var assertionError = expectAssertionError(() -> assertThat(actual).hasDigest(algorithm, expected));
      // THEN
      then(assertionError).hasMessage(shouldHaveDigest(actual,
                                                       new DigestDiff(digest, toHex(expected), getInstance(algorithm))).create());
    }

  }

  @Nested
  class With_String_and_String {

    @Test
    void should_fail_if_actual_is_null() {
      // GIVEN
      InputStream actual = null;
      String algorithm = "MD5";
      String digest = "00000000000000000000000000000000";
      // WHEN
      var assertionError = expectAssertionError(() -> assertThat(actual).hasDigest(algorithm, digest));
      // THEN
      then(assertionError).hasMessage(shouldNotBeNull().create());
    }

    @Test
    void should_fail_if_algorithm_is_null() {
      // GIVEN
      InputStream actual = new ByteArrayInputStream(new byte[0]);
      String algorithm = null;
      String digest = "00000000000000000000000000000000";
      // WHEN
      Exception exception = catchException(() -> assertThat(actual).hasDigest(algorithm, digest));
      // THEN
      then(exception).isInstanceOf(NullPointerException.class)
                     .hasMessage(shouldNotBeNull("algorithm").create());
    }

    @Test
    void should_fail_if_algorithm_is_invalid() {
      // GIVEN
      InputStream actual = new ByteArrayInputStream(new byte[0]);
      String algorithm = "non-existing";
      String digest = "00000000000000000000000000000000";
      // WHEN
      Exception exception = catchException(() -> assertThat(actual).hasDigest(algorithm, digest));
      // THEN
      then(exception).isInstanceOf(IllegalArgumentException.class)
                     .cause().isInstanceOf(NoSuchAlgorithmException.class);
    }

    @Test
    void should_fail_if_digest_is_null() {
      // GIVEN
      InputStream actual = new ByteArrayInputStream(new byte[0]);
      String algorithm = "MD5";
      String digest = null;
      // WHEN
      Exception exception = catchException(() -> assertThat(actual).hasDigest(algorithm, digest));
      // THEN
      then(exception).isInstanceOf(NullPointerException.class)
                     .hasMessage(shouldNotBeNull("digest").create());
    }

    @Test
    void should_rethrow_IOException() throws Exception {
      // GIVEN
      @SuppressWarnings("resource")
      InputStream actual = mock();
      String algorithm = "MD5";
      String digest = "00000000000000000000000000000000";
      IOException cause = new IOException();
      given(actual.read(any())).willThrow(cause);
      // WHEN
      Exception exception = catchException(() -> assertThat(actual).hasDigest(algorithm, digest));
      // THEN
      then(exception).isInstanceOf(UncheckedIOException.class)
                     .hasCause(cause);
    }

    @ParameterizedTest
    @Parameters
    void should_pass_resetting_actual_if_actual_has_expected_digest_and_supports_marking(@Hex byte[] input, String algorithm,
                                                                                         String expected) {
      // GIVEN
      InputStream actual = new ByteArrayInputStream(input);
      // WHEN
      assertThat(actual).hasDigest(algorithm, expected);
      // THEN
      then(actual).isNotEmpty();
    }

    @ParameterizedTest
    @Parameters
    void should_pass_without_resetting_actual_if_actual_has_expected_digest_and_does_not_support_marking(@Hex byte[] input,
                                                                                                         String algorithm,
                                                                                                         String expected) {
      // GIVEN
      InputStream actual = new UnmarkableByteArrayInputStream(input);
      // WHEN
      assertThat(actual).hasDigest(algorithm, expected);
      // THEN
      then(actual).isEmpty();
    }

    @ParameterizedTest
    @Parameters
    void should_fail_if_actual_does_not_have_expected_digest(@Hex byte[] input, String algorithm,
                                                             String digest) throws Exception {
      // GIVEN
      InputStream actual = new ByteArrayInputStream(input);
      String expected = "00000000000000000000000000000000";
      // WHEN
      var assertionError = expectAssertionError(() -> assertThat(actual).hasDigest(algorithm, expected));
      // THEN
      then(assertionError).hasMessage(shouldHaveDigest(actual,
                                                       new DigestDiff(digest, expected, getInstance(algorithm))).create());
    }

  }

  @Target(METHOD)
  @Retention(RUNTIME)
  @CsvSource({
      "89504E470D0A1A0A0000000D49484452, MD5, 7CDDABE5DF64DAAA6924A5613DD2150A",
  })
  @interface Parameters {
  }

}
