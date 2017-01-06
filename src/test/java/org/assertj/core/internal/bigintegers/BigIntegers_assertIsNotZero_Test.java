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
package org.assertj.core.internal.bigintegers;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.BigIntegers;
import org.assertj.core.internal.BigIntegersBaseTest;
import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

import java.math.BigInteger;

import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.test.TestData.someInfo;


/**
 * Tests for <code>{@link BigIntegers#assertIsNotZero(AssertionInfo, BigInteger)}</code>.
 * 
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class BigIntegers_assertIsNotZero_Test extends BigIntegersBaseTest {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_succeed_since_actual_is_zero() {
    numbers.assertIsNotZero(someInfo(), BigInteger.ONE);
  }

  @Test
  public void should_fail_since_actual_is_not_zero() {
    thrown.expectAssertionError("%nExpecting:%n <0>%nnot to be equal to:%n <0>%n");
    numbers.assertIsNotZero(someInfo(), BigInteger.ZERO);
  }

  @Test
  public void should_succeed_since_actual_is_zero_whatever_custom_comparison_strategy_is() {
    numbersWithComparatorComparisonStrategy.assertIsNotZero(someInfo(), BigInteger.ONE);
  }

  @Test
  public void should_fail_since_actual_is_not_zero_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError("%nExpecting:%n <0>%nnot to be equal to:%n <0>%n");
    numbersWithComparatorComparisonStrategy.assertIsNotZero(someInfo(), BigInteger.ZERO);
  }

}
