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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Digests#fromHex(String)}</code>.
 *
 * @author Valeriy Vyrva
 */
public class Digests_fromHex_Test extends DigestsBaseTest {

  @Test
  public void should_fail_if_digest_is_null() {
    assertThatNullPointerException().isThrownBy(() -> Digests.fromHex(null))
                                    .withMessage("The digest should not be null");
  }

  @Test
  public void should_pass_if_digest_is_empty() {
    assertThat(Digests.fromHex("")).isEmpty();
  }

  @Test
  public void should_pass_if_digest_converted_correctly() {
    assertThat(Digests.fromHex(DIGEST_TEST_1_STR)).isEqualTo(DIGEST_TEST_1_BYTES);
  }

  @Test
  public void should_fail_if_digest_converted_incorrectly() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(Digests.fromHex(EXPECTED_MD5_DIGEST_STR)).isEqualTo(DIGEST_TEST_1_BYTES));
  }

  @Test
  public void should_pass_if_digest_length_is_not_even() {
    assertThat(Digests.fromHex("A")).isEmpty();
    assertThat(Digests.fromHex("AA")).containsExactly(170);
    assertThat(Digests.fromHex("AAA")).containsExactly(170);
  }
}
