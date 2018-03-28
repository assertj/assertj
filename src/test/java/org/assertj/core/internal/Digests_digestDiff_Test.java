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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for <code>{@link Digests#digestDiff(InputStream, MessageDigest, byte[])}</code>.
 *
 * @author Valeriy Vyrva
 */
public class Digests_digestDiff_Test extends DigestsBaseTest {

  private InputStream stream;
  private MessageDigest digest;
  private byte[] expected = new byte[]{0, 1};

  @Before
  public void init() {
    stream = mock(InputStream.class);
    digest = mock(MessageDigest.class);
  }

  @Test
  public void should_fail_if_stream_is_null() throws IOException {
  thrown.expectNullPointerException("The stream should not be null");
  Digests.digestDiff(null, null, null);
  }

  @Test
  public void should_fail_if_digest_is_null() throws IOException {
  thrown.expectNullPointerException("The digest should not be null");
  Digests.digestDiff(stream, null, null);
  }

  @Test
  public void should_fail_if_expected_is_null() throws IOException {
  thrown.expectNullPointerException("The expected should not be null");
  Digests.digestDiff(stream, digest, null);
  }

  //todo should_error_if_IO

  @Test
  public void should_pass_if_stream_is_readable() throws IOException {
  when(digest.digest()).thenReturn(expected);
  Digests.digestDiff(stream, digest, expected);
  }

  @Test
  public void should_pass_if_digest_is_MD5() throws IOException, NoSuchAlgorithmException {
  DigestDiff diff = Digests.digestDiff(
    getClass().getResourceAsStream("/red.png"),
    MessageDigest.getInstance("MD5"),
    DIGEST_TEST_2_BYTES
  );
  assertThat(diff.isEquals()).isTrue();
  }

  @Test
  public void should_pass_if_digest_is_MD5_and_unclear() throws IOException, NoSuchAlgorithmException {
  MessageDigest digest = MessageDigest.getInstance("MD5");
  digest.update(expected);
  DigestDiff diff = Digests.digestDiff(
    getClass().getResourceAsStream("/red.png"),
    digest,
    DIGEST_TEST_2_BYTES
  );
  assertThat(diff.isEquals()).isTrue();
  }
}
