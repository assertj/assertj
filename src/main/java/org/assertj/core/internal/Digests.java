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
package org.assertj.core.internal;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.util.Hexadecimals.byteToHexString;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * Reusable utils for digest processing
 *
 * @author Valeriy Vyrva
 */
public final class Digests {

  private static final int BUFFER_SIZE = 1024 * 8;

  private Digests() {
  }

  public static String toHex(byte[] digest) {
    requireNonNull(digest, "The digest should not be null");
    StringBuilder hex = new StringBuilder(digest.length * 2);
    for (byte b : digest) {
      hex.append(byteToHexString(b));
    }
    return hex.toString();
  }

  public static byte[] fromHex(String digest) {
    requireNonNull(digest, "The digest should not be null");
    byte[] bytes = new byte[digest.length() / 2];
    for (int i = 0; i < bytes.length; i++) {
      bytes[i] = Integer.valueOf(digest.substring(i * 2, (i + 1) * 2), 16).byteValue();
    }
    return bytes;
  }

  public static DigestDiff digestDiff(InputStream stream, MessageDigest messageDigest, byte[] expected) throws IOException {
    requireNonNull(stream, "The stream should not be null");
    requireNonNull(messageDigest, "The digest should not be null");
    requireNonNull(expected, "The expected should not be null");
    messageDigest.reset();
    byte[] buffer = new byte[BUFFER_SIZE];
    int len;
    while ((len = stream.read(buffer)) > 0) {
      messageDigest.update(buffer, 0, len);
    }
    byte[] actualDigest = messageDigest.digest();
    String expectedHex = toHex(expected);
    String actualHex = toHex(actualDigest);
    return new DigestDiff(actualHex, expectedHex, messageDigest);
  }
}
