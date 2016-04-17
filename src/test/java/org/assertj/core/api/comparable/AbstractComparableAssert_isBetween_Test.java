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
package org.assertj.core.api.comparable;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;

import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

public class AbstractComparableAssert_isBetween_Test {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void succeds_if_actual_is_between_start_and_end() {
    assertThat(BigInteger.ONE).isBetween(BigInteger.ZERO, BigInteger.TEN);
  }

  @Test
  public void succeds_if_actual_is_equal_to_start() {
    assertThat('a').isBetween('a', 'b');
  }

  @Test
  public void succeds_if_actual_is_equal_to_end() {
    assertThat('b').isBetween('a', 'b');
  }

  @Test
  public void fails_if_actual_is_less_than_start() {
    thrown.expectAssertionError("Expecting:%n <'a'>%nto be between:%n ['b', 'c']");
    assertThat('a').isBetween('b', 'c');
  }

  @Test
  public void fails_if_actual_is_greater_than_end() {
    thrown.expectAssertionError("Expecting:%n <'c'>%nto be between:%n ['a', 'b']");
    assertThat('c').isBetween('a', 'b');
  }
}
