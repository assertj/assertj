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
package org.assertj.core.api.intarray;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.test.IntArrays.arrayOf;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.IntArrayAssert;
import org.assertj.core.api.IntArrayAssertBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link IntArrayAssert#containsOnly(Integer[])}</code>.
 *
 * @author Omar Morales Ortega
 */
class IntArrayAssert_containsOnly_with_Integer_array_Test extends IntArrayAssertBaseTest {

  @Test
  void should_fail_if_values_is_null() {
    // GIVEN
    Integer[] values = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertions.containsOnly(values));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage(shouldNotBeNull("values").create());
  }

  @Override
  protected IntArrayAssert invoke_api_method() {
    return assertions.containsOnly(new Integer[] { 1, 2 });
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertContainsOnly(getInfo(assertions), getActual(assertions), arrayOf(1, 2));
  }

}
