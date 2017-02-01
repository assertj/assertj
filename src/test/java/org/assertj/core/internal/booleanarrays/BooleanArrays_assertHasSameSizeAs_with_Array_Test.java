/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal.booleanarrays;

import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.BooleanArrays;
import org.assertj.core.internal.BooleanArraysBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link BooleanArrays#assertHasSameSizeAs(AssertionInfo, boolean[], Object[])}</code>.
 * 
 * @author Nicolas François
 * @author Joel Costigliola
 */
public class BooleanArrays_assertHasSameSizeAs_with_Array_Test extends BooleanArraysBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertHasSameSizeAs(someInfo(), null, array("Solo", "Leia"));
  }

  @Test
  public void should_fail_if_size_of_actual_is_not_equal_to_expected_size() {
    AssertionInfo info = someInfo();
    String[] other = array("Solo", "Leia", "Yoda");

    thrown.expectAssertionError(shouldHaveSameSizeAs(actual, actual.length, other.length)
                                .create(null, info.representation()));

    arrays.assertHasSameSizeAs(info, actual, other);
  }

  @Test
  public void should_pass_if_size_of_actual_is_equal_to_expected_size() {
    arrays.assertHasSameSizeAs(someInfo(), actual, array("Solo", "Leia"));
  }
}
