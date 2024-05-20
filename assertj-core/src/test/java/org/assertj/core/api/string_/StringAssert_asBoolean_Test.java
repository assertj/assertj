/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.string_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.StringAssert;
import org.assertj.core.api.StringAssertBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for <code>{@link StringAssert#asBoolean()}</code>.
 *
 * @author hezean
 */
class StringAssert_asBoolean_Test extends StringAssertBaseTest {

  @Override
  protected StringAssert invoke_api_method() {
    assertions.asBoolean();
    return null;
  }

  @Override
  protected void verify_internal_effects() {
    // Verify disabled as the asBoolean cast have no internal effect.
  }

  @Override
  public void should_return_this() {
    // Test disabled as the assertion does not return this.
  }

  @ParameterizedTest
  @ValueSource(strings = { "true", "TruE" })
  void should_parse_string_as_true_for_true_like_string(String string) {
    assertThat(string).asBoolean().isTrue();
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { " false", "foo bar" })
  void should_parse_string_as_false_otherwise(String string) {
    assertThat(string).asBoolean().isFalse();
  }

  @Test
  void should_work_with_soft_assertions() {
    // GIVEN
    SoftAssertions softAssertions = new SoftAssertions();
    // WHEN
    softAssertions.assertThat("true").asBoolean()
                  .as("false 1").isFalse()
                  .as("false 2").isFalse();
    // THEN
    List<Throwable> errorsCollected = softAssertions.errorsCollected();
    then(errorsCollected).hasSize(2);
    then(errorsCollected.get(0)).hasMessageContaining("false 1");
    then(errorsCollected.get(1)).hasMessageContaining("false 2");
  }
}
