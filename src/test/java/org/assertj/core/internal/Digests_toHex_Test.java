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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import org.junit.Test;

/**
 * Tests for <code>{@link Digests#toHex(byte[])}</code>.
 *
 * @author Valeriy Vyrva
 */
public class Digests_toHex_Test extends DigestsBaseTest {

  @Test
  public void should_fail_if_digest_is_null() {
    assertThatNullPointerException().isThrownBy(() -> Digests.toHex(null))
                                    .withMessage("The digest should not be null");
  }

  @Test
  public void should_pass_if_digest_is_empty() {
    assertThat(Digests.toHex(new byte[0])).isEqualTo("");
  }

  @Test
  public void should_pass_if_digest_is_correctly_converted() {
    assertThat(Digests.toHex(DIGEST_TEST_1_BYTES)).isEqualTo(DIGEST_TEST_1_STR);
  }

  @Test
  public void should_fail_if_digest_conversion__incorrect() {
    thrown.expectAssertionError(shouldBeEqual(DIGEST_TEST_1_STR, EXPECTED_MD5_DIGEST_STR, STANDARD_REPRESENTATION));
    assertThat(Digests.toHex(DIGEST_TEST_1_BYTES)).isEqualTo(EXPECTED_MD5_DIGEST_STR);
  }
}
