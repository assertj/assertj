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
package org.assertj.tests.core.api.string_;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeBase64.shouldBeBase64;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractByteArrayAssert;
import org.assertj.core.api.StringAssert;
import org.assertj.tests.core.testkit.NavigationMethodBaseTest;
import org.junit.jupiter.api.Test;

/// @author Stefano Cordio
class StringAssert_asBase64Decoded_Test implements NavigationMethodBaseTest<StringAssert> {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).asBase64Decoded());
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_has_invalid_Base64_characters() {
    // GIVEN
    String actual = "inv@lid";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).asBase64Decoded());
    // THEN
    then(assertionError).hasMessage(shouldBeBase64(actual).create());
  }

  @Test
  void should_decode_actual_Base64_encoded_with_padding() {
    assertThat("QXNzZXJ0Sg==").asBase64Decoded().containsExactly("AssertJ".getBytes(UTF_8));
  }

  @Test
  void should_decode_actual_Base64_encoded_without_padding() {
    assertThat("QXNzZXJ0Sg").asBase64Decoded().containsExactly("AssertJ".getBytes(UTF_8));
  }

  @Test
  void should_return_byte_array_assertion() {
    // GIVEN
    String actual = "QXNzZXJ0Sg==";
    // WHEN
    AbstractAssert<?, ?> result = assertThat(actual).asBase64Decoded();
    // THEN
    then(result).isInstanceOf(AbstractByteArrayAssert.class);
  }

  @Override
  public StringAssert getAssertion() {
    return new StringAssert("QXNzZXJ0Sg==");
  }

  @Override
  public AbstractAssert<?, ?> invoke_navigation_method(StringAssert assertion) {
    return assertion.asBase64Decoded();
  }

}
