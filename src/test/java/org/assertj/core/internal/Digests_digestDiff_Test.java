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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.internal.Digests.digestDiff;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Digests#digestDiff(InputStream, MessageDigest, byte[])}</code>.
 *
 * @author Valeriy Vyrva
 */
public class Digests_digestDiff_Test extends DigestsBaseTest {

  private InputStream stream;
  private MessageDigest digest;
  private byte[] expected = new byte[] { 0, 1 };

  @BeforeEach
  public void init() {
    stream = mock(InputStream.class);
    digest = mock(MessageDigest.class);
  }

  @Test
  public void should_fail_if_stream_is_null() {
    assertThatNullPointerException().isThrownBy(() -> digestDiff(null, null, null))
                                    .withMessage("The stream should not be null");
  }

  @Test
  public void should_fail_if_digest_is_null() {
    assertThatNullPointerException().isThrownBy(() -> digestDiff(stream, null, null))
                                    .withMessage("The digest should not be null");
  }

  @Test
  public void should_fail_if_expected_is_null() {
    assertThatNullPointerException().isThrownBy(() -> digestDiff(stream, digest, null))
                                    .withMessage("The expected should not be null");
  }

  // todo should_error_if_IO

  @Test
  public void should_pass_if_stream_is_readable() throws IOException {
    // GIVEN
    given(digest.digest()).willReturn(expected);
    // THEN
    digestDiff(stream, digest, expected);
  }

  @Test
  public void should_pass_if_digest_is_MD5() throws IOException, NoSuchAlgorithmException {
    // GIVEN
    InputStream inputStream = getClass().getResourceAsStream("/red.png");
    // WHEN
    DigestDiff diff = digestDiff(inputStream, MessageDigest.getInstance("MD5"), EXPECTED_MD5_DIGEST);
    // THEN
    assertThat(diff.digestsDiffer()).isFalse();
  }

  @Test
  public void should_pass_if_digest_is_MD5_and_updated() throws IOException, NoSuchAlgorithmException {
    // GIVEN
    InputStream inputStream = getClass().getResourceAsStream("/red.png");
    MessageDigest digest = MessageDigest.getInstance("MD5");
    digest.update(expected);
    // WHEN
    DigestDiff diff = digestDiff(inputStream, digest, EXPECTED_MD5_DIGEST);
    // THEN
    assertThat(diff.digestsDiffer()).isFalse();
  }
}
