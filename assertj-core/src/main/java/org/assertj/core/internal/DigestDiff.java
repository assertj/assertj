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
package org.assertj.core.internal;

import java.security.MessageDigest;

/**
 * Compares the digest values
 *
 * @author Valeriy Vyrva
 */
public class DigestDiff {
  private final MessageDigest digest;
  private final String expected;
  private final String actual;

  /**
   * Creates a digest comparison result.
   *
   * @param actual the actual digest
   * @param expected the expected digest
   * @param digest the message digest algorithm
   */
  public DigestDiff(String actual, String expected, MessageDigest digest) {
    this.digest = digest;
    this.expected = expected;
    this.actual = actual;
  }

  /**
   * Returns the expected digest.
   *
   * @return the expected digest
   */
  public String getExpected() {
    return expected;
  }

  /**
   * Returns the actual digest.
   *
   * @return the actual digest
   */
  public String getActual() {
    return actual;
  }

  /**
   * Checks whether the digests differ.
   *
   * @return whether the digests differ
   */
  public boolean digestsDiffer() {
    return !expected.equals(actual);
  }

  /**
   * Returns the digest algorithm name.
   *
   * @return the digest algorithm
   */
  public String getDigestAlgorithm() {
    return digest.getAlgorithm();
  }
}
