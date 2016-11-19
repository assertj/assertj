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

import org.assertj.core.error.ShouldHaveValue;
import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

public class AtomicInteger_assertions_Test {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void should_accept_null_atomicInteger() {
    AtomicInteger actual = null;
    assertThat(actual).isNull();
    then(actual).isNull();
  }

  @Test
  public void should_be_able_to_use_any_int_assertions() {
    AtomicInteger actual = new AtomicInteger(123);
    assertThat(actual).isLessThan(1234)
                      .isGreaterThan(12)
                      .isNotEqualTo(123);
    then(actual).isNotEqualTo(1234);
  }

  @Test
  public void should_be_able_to_use_EqualTo_AtomicInteger() {
    AtomicInteger actual = new AtomicInteger(123);
    assertThat(actual).isEqualTo(actual);
  }

  @Test
  public void should_fail_if_atomicInteger_does_not_contain_expected_value() {
    AtomicInteger actual = new AtomicInteger(123);
    int expectedValue = 1234;
    thrown.expectAssertionError(ShouldHaveValue.shouldHaveValue(actual, expectedValue).create());
    assertThat(actual).hasValue(1234);
  }

  @Test
  public void should_be_able_to_use_isPositive() {
    AtomicInteger actual = new AtomicInteger(123);
    assertThat(actual).isPositive();
  }

  @Test
  public void should_be_able_to_use_isNegative() {
    AtomicInteger actual = new AtomicInteger(-123);
    assertThat(actual).isNegative();
  }

  @Test
  public void should_be_able_to_use_isBetween() {
    AtomicInteger actual = new AtomicInteger(123);
    assertThat(actual).isBetween(99, 149);
  }

}
