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
package org.assertj.core.api.shortarray;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.test.ShortArrays.arrayOf;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.ShortArrayAssert;
import org.assertj.core.api.ShortArrayAssertBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShortArrayAssert#endsWith(Short[])}</code>.
 *
 * @author Lucero Garcia
 */
class ShortArrayAssert_endsWith_with_Short_array_Test extends ShortArrayAssertBaseTest {

  @Test
  void should_fail_if_values_is_null() {
    // GIVEN
    Short[] sequence = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertions.endsWith(sequence));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage(shouldNotBeNull("sequence").create());
  }

  @Override
  protected ShortArrayAssert invoke_api_method() {
    return assertions.endsWith(new Short[] { 2, 3 });
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertEndsWith(getInfo(assertions), getActual(assertions), arrayOf(2, 3));
  }

}
