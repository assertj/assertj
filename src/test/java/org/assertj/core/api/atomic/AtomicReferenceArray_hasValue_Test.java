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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.api.atomic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;
import static org.assertj.core.error.ShouldContainAtIndex.shouldContainAtIndex;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.concurrent.atomic.AtomicReferenceArray;

import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

public class AtomicReferenceArray_hasValue_Test {

  private static final String ACTUAL = "actual";
  private static final String EXPECTED = "expectedValue";

  @Rule
  public ExpectedException thrown = ExpectedException.none();


  @Test
  public void should_fail_when_atomicReferenceArray_is_null() throws Exception {
    thrown.expectAssertionError(actualIsNull());

    assertThat((AtomicReferenceArray<String>) null).hasValue(EXPECTED, atIndex(1));
  }

  @Test
  public void should_fail_if_expected_value_is_null_and_does_not_contain_expected_value() throws Exception {
    String[] array = array(ACTUAL);
    AtomicReferenceArray<String> actual = new AtomicReferenceArray<>(array);
    thrown.expectAssertionError(shouldContainAtIndex(array, null, atIndex(0), ACTUAL).create());

    assertThat(actual).hasValue(null, atIndex(0));
  }

  @Test
  public void should_fail_if_atomicReferenceArray_does_not_contain_expected_value() throws Exception {
    String[] array = array(ACTUAL);
    AtomicReferenceArray<String> actual = new AtomicReferenceArray<>(array);
    thrown.expectAssertionError(shouldContainAtIndex(array, EXPECTED, atIndex(0), ACTUAL).create());

    assertThat(actual).hasValue(EXPECTED, atIndex(0));
  }

  @Test
  public void should_pass_if_atomicReferenceArray_contains_expected_value() throws Exception {
    AtomicReferenceArray<String> actual = new AtomicReferenceArray<>(array(ACTUAL, EXPECTED));
    assertThat(actual).hasValue(EXPECTED, atIndex(1));
  }
}
