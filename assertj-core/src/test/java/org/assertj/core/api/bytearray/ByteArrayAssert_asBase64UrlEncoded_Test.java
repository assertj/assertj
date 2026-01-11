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
package org.assertj.core.api.bytearray;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.nio.charset.StandardCharsets;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.ByteArrayAssert;
import org.assertj.core.api.ByteArrayAssertBaseTest;
import org.assertj.core.api.NavigationMethodWithComparatorBaseTest;
import org.junit.jupiter.api.Test;

class ByteArrayAssert_asBase64UrlEncoded_Test extends ByteArrayAssertBaseTest
    implements NavigationMethodWithComparatorBaseTest<ByteArrayAssert> {

  @Override
  protected ByteArrayAssert invoke_api_method() {
    assertions.asBase64UrlEncoded();
    return null;
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertNotNull(getInfo(assertions), getActual(assertions));
  }

  @Override
  public void should_return_this() {
    // Test disabled as the assertion does not return this.
  }

  @Override
  public ByteArrayAssert getAssertion() {
    return assertions;
  }

  @Override
  public AbstractAssert<?, ?> invoke_navigation_method(ByteArrayAssert assertion) {
    return assertion.asBase64UrlEncoded();
  }

  @Test
  void should_return_string_assertion() {
    // WHEN
    AbstractAssert<?, ?> result = assertions.asBase64UrlEncoded();
    // THEN
    then(result).isInstanceOf(AbstractStringAssert.class);
  }

  @Test
  void should_produce_valid_base64Url() {
    // GIVEN
    byte[] bytes = "AssertJ ?".getBytes(StandardCharsets.UTF_8);
    // WHEN/THEN
    then(bytes).asBase64UrlEncoded()
               .isEqualTo("QXNzZXJ0SiA_");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    byte[] bytes = null;
    // WHEN
    var assertionError = expectAssertionError(() -> then(bytes).asBase64UrlEncoded());
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }
}
