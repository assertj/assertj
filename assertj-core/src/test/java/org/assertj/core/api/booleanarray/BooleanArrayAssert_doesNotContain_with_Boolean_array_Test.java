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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api.booleanarray;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.test.BooleanArrays.arrayOf;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.BooleanArrayAssert;
import org.assertj.core.api.BooleanArrayAssertBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link BooleanArrayAssert#doesNotContain(Boolean[])}</code>.
 * 
 * @author Stefano Cordio
 */
@DisplayName("BooleanArrayAssert doesNotContain(Boolean[])")
class BooleanArrayAssert_doesNotContain_with_Boolean_array_Test extends BooleanArrayAssertBaseTest {

  @Test
  void should_fail_if_values_is_null() {
    // GIVEN
    Boolean[] values = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertions.doesNotContain(values));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage(shouldNotBeNull("values").create());
  }

  @Override
  protected BooleanArrayAssert invoke_api_method() {
    return assertions.doesNotContain(new Boolean[] { true, false });
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertDoesNotContain(getInfo(assertions), getActual(assertions), arrayOf(true, false));
  }

}
