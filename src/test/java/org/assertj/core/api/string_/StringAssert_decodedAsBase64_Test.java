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
package org.assertj.core.api.string_;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractByteArrayAssert;
import org.assertj.core.api.AbstractSoftAssertions;
import org.assertj.core.api.ProxyableObjectChangingMethodTest;
import org.assertj.core.api.StringAssert;
import org.assertj.core.api.StringAssertBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link StringAssert#decodedAsBase64()}</code>.
 *
 * @author Stefano Cordio
 */
@DisplayName("StringAssert decodedAsBase64")
class StringAssert_decodedAsBase64_Test extends StringAssertBaseTest implements ProxyableObjectChangingMethodTest<StringAssert> {

  @Override
  protected StringAssert invoke_api_method() {
    assertions.decodedAsBase64();
    return null;
  }

  @Override
  protected void verify_internal_effects() {
    verify(strings).assertIsBase64(getInfo(assertions), getActual(assertions));
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
  public StringAssert getSoftAssertion(AbstractSoftAssertions softly) {
    return softly.proxy(StringAssert.class, String.class, getActual(assertions));
  }

  @Override
  public AbstractAssert<?, ?> invoke_object_changing_method(StringAssert assertion) {
    return assertion.decodedAsBase64();
  }

  @Test
  void should_return_byte_array_assertion() {
    // WHEN
    AbstractAssert<?, ?> result = assertions.decodedAsBase64();
    // THEN
    then(result).isInstanceOf(AbstractByteArrayAssert.class);
  }

}
