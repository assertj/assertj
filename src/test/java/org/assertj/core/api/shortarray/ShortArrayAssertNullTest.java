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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.shortarray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.BDDAssertions.then;

import org.assertj.core.api.AbstractShortArrayAssert;
import org.assertj.core.api.ShortArrayAssertBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Adds a test to verify null behavior against empty arrays.
 * 
 * Should be used for methods with an int[] or int... parameter.
 * 
 * @author Dan Avila
 */
public abstract class ShortArrayAssertNullTest extends ShortArrayAssertBaseTest {

  @Test
  public void should_throw_exception_on_null_argument() {
    // GIVEN
    int[] nullContent = null;
    // WHEN
    NullPointerException npe = catchThrowableOfType(() -> invoke_api_with_null_value(assertThat(new short[] {}), nullContent),
                                                    NullPointerException.class);
    // THEN
    then(npe).hasMessage("The array of values to look for should not be null");
  }

  protected abstract void invoke_api_with_null_value(AbstractShortArrayAssert<?> emptyAssert, int[] nullArray);
}
