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
package org.assertj.core.internal.objectarrays;

import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.core.internal.ObjectArraysBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link ObjectArrays#assertHasSameSizeAs(AssertionInfo, Object[], Iterable)}</code>.
 * 
 * @author Nicolas François
 * @author Joel Costigliola
 */
public class ObjectArrays_assertHasSameSizeAs_with_Iterable_Test extends ObjectArraysBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertHasSameSizeAs(someInfo(), null, newArrayList("Solo", "Leia"));
  }

  @Test
  public void should_fail_if_other_is_null() {
    thrown.expectNullPointerException("The Iterable to compare actual size with should not be null");
    String[] actual = array("Solo", "Leia");
    Iterable<?> other = null;
    arrays.assertHasSameSizeAs(someInfo(), actual, other);
  }

  @Test
  public void should_fail_if_actual_size_is_not_equal_to_other_size() {
    AssertionInfo info = someInfo();
    String[] actual = array("Yoda");
    List<String> other = newArrayList("Solo", "Leia");

    thrown.expectAssertionError(shouldHaveSameSizeAs(actual, actual.length, other.size())
                                .create(null, info.representation()));

    arrays.assertHasSameSizeAs(info, actual, other);
  }

  @Test
  public void should_pass_if_actual_has_same_size_as_other() {
    arrays.assertHasSameSizeAs(someInfo(), array("Solo", "Leia"), newArrayList("Solo", "Leia"));
  }
}
