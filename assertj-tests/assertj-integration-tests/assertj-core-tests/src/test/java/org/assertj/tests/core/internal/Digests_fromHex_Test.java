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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.core.internal;

import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;

import org.assertj.core.internal.Digests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author Valeriy Vyrva
 */
class Digests_fromHex_Test {

  private static final byte[] DIGEST_TEST_1_BYTES = { -38, 57, -93, -18, 94, 107, 75, 13, 50, 85, -65, -17, -107, 96, 24, -112,
      -81, -40, 7, 9 };
  private static final String DIGEST_TEST_1_STR = "DA39A3EE5E6B4B0D3255BFEF95601890AFD80709";

  @Test
  void should_fail_if_digest_is_null() {
    // WHEN
    NullPointerException exception = catchNullPointerException(() -> Digests.fromHex(null));
    // THEN
    then(exception).hasMessage(shouldNotBeNull("digest").create());
  }

  @ParameterizedTest
  @ValueSource(strings = { "", "A" })
  void should_return_empty_array_if_digest_is_empty(String digest) {
    // WHEN
    byte[] result = Digests.fromHex(digest);
    // THEN
    then(result).isEmpty();
  }

  @Test
  void should_return_non_empty_array_if_digest_is_not_empty() {
    // WHEN
    byte[] result = Digests.fromHex(DIGEST_TEST_1_STR);
    // THEN
    then(result).isEqualTo(DIGEST_TEST_1_BYTES);
  }

  @ParameterizedTest
  @ValueSource(strings = { "AA", "AAA" })
  void should_return_non_empty_array_if_digest_length_is_not_even(String digest) {
    // WHEN
    byte[] result = Digests.fromHex(digest);
    // THEN
    then(result).containsExactly(170);
  }

}
