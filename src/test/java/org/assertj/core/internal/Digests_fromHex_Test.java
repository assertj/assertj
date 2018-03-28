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

import org.assertj.core.presentation.Representation;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;

/**
 * Tests for <code>{@link Digests#fromHex(String)}</code>.
 *
 * @author Valeriy Vyrva
 */
public class Digests_fromHex_Test extends DigestsBaseTest {

  @Test
  public void should_fail_if_digest_is_null() {
  thrown.expectNullPointerException("The digest should not be null");
  Digests.fromHex(null);
  }

  @Test
  public void should_pass_if_digest_is_empty() {
  assertThat(Digests.fromHex("")).isEqualTo(new byte[0]);
  }

  @Test
  public void should_pass_if_digest_converted_correctly() {
  assertThat(Digests.fromHex(DIGEST_TEST_1_STR)).containsExactly(DIGEST_TEST_1_BYTES);
  }

  @Test
  public void should_fail_if_digest_converted_incorrectly() {
  Representation representation = StandardRepresentation.STANDARD_REPRESENTATION;
  thrown.expectAssertionError(shouldBeEqual(DIGEST_TEST_2_BYTES, DIGEST_TEST_1_BYTES, StandardComparisonStrategy.instance(), representation));
  assertThat(Digests.fromHex(DIGEST_TEST_2_STR)).containsExactly(DIGEST_TEST_1_BYTES);
  }

  @Test
  public void should_pass_if_digest_length_is_not_even() {
  assertThat(Digests.fromHex("A")).isEmpty();
  assertThat(Digests.fromHex("AA")).containsExactly(170);
  assertThat(Digests.fromHex("AAA")).containsExactly(170);
  }
}
