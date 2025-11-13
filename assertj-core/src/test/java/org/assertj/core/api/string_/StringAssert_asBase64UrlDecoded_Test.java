/*
 * Copyright 2012-2025 the original author or authors.
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
package org.assertj.core.api.string_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.nio.charset.StandardCharsets;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractByteArrayAssert;
import org.assertj.core.api.NavigationMethodBaseTest;
import org.assertj.core.api.StringAssert;
import org.assertj.core.api.StringAssertBaseTest;
import org.junit.jupiter.api.Test;

class StringAssert_asBase64UrlDecoded_Test extends StringAssertBaseTest implements NavigationMethodBaseTest<StringAssert> {

  @Override
  protected StringAssert invoke_api_method() {
    assertions.asBase64UrlDecoded();
    return null;
  }

  @Override
  protected void verify_internal_effects() {
    verify(strings).assertIsBase64Url(getInfo(assertions), getActual(assertions));
  }

  @Override
  public void should_return_this() {
    // Test disabled as the assertion does not return this.
  }

  @Override
  public StringAssert getAssertion() {
    return assertions;
  }

  @Override
  public AbstractAssert<?, ?> invoke_navigation_method(StringAssert assertion) {
    return assertion.asBase64UrlDecoded();
  }

  @Test
  void should_return_byte_array_assertion() {
    // WHEN
    AbstractAssert<?, ?> result = assertions.asBase64UrlDecoded();
    // THEN
    then(result).isInstanceOf(AbstractByteArrayAssert.class);
  }

  @Test
  void should_accept_valid_base64Url() {
    // GIVEN
    String base64Url = "QXNzZXJ0SiA_";
    // WHEN/THEN
    then(base64Url).asBase64UrlDecoded()
                   .isEqualTo("AssertJ ?".getBytes(StandardCharsets.UTF_8));
  }

  @Test
  void should_reject_invalid_base64Url() {
    // GIVEN
    String base64Url = "hello";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(base64Url).asBase64UrlDecoded());
    // THEN
    then(assertionError).hasMessageContaining("Expecting \"hello\" to be a valid Base64 URL encoded string");
  }

  @Test
  void should_reject_regular_base64() {
    // GIVEN
    String base64 = "c3ViamVjdHM/X2Q9MQ==";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(base64).asBase64UrlDecoded());
    // THEN
    then(assertionError).hasMessageContaining("Expecting \"c3ViamVjdHM/X2Q9MQ==\" to be a valid Base64 URL encoded string");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String string = null;
    // WHEN
    var assertionError = expectAssertionError(() -> then(string).asBase64UrlDecoded());
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }
}
