/*
 * Created on Oct 20, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.internal;

import org.assertj.core.api.AssertionInfo;

/**
 * Base class of reusable assertions for numbers.
 * 
 * @author Joel Costigliola
 * @author Nicolas François
 */
public abstract class Numbers<NUMBER extends Comparable<NUMBER>> extends Comparables {

  public Numbers() {
    super();
  }

  public Numbers(ComparisonStrategy comparisonStrategy) {
    super(comparisonStrategy);
  }

  protected abstract NUMBER zero();

  /**
   * Asserts that the actual value is equal to zero.<br>
   * It does not rely on the custom comparisonStrategy (if one is set).
   * 
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to zero.
   */
  public void assertIsZero(AssertionInfo info, NUMBER actual) {
    assertEqualByComparison(info, actual, zero());
  }

  /**
   * Asserts that the actual value is not equal to zero.<br>
   * It does not rely on the custom comparisonStrategy (if one is set).
   * 
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to zero.
   */
  public void assertIsNotZero(AssertionInfo info, NUMBER actual) {
    assertNotEqualByComparison(info, actual, zero());
  }

  /**
   * Asserts that the actual value is negative.
   * 
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not negative: it is either equal to or greater than zero.
   */
  public void assertIsNegative(AssertionInfo info, NUMBER actual) {
    assertLessThan(info, actual, zero());
  }

  /**
   * Asserts that the actual value is positive.
   * 
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not positive: it is either equal to or less than zero.
   */
  public void assertIsPositive(AssertionInfo info, NUMBER actual) {
    assertGreaterThan(info, actual, zero());
  }

  /**
   * Asserts that the actual value is not negative.
   * 
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is negative.
   */
  public void assertIsNotNegative(AssertionInfo info, NUMBER actual) {
    assertGreaterThanOrEqualTo(info, actual, zero());
  }

  /**
   * Asserts that the actual value is not positive.
   * 
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is positive.
   */
  public void assertIsNotPositive(AssertionInfo info, NUMBER actual) {
    assertLessThanOrEqualTo(info, actual, zero());
  }

  /**
   * Asserts that the actual value is in [start, end] range (start included, end included).
   * 
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is positive.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   * @throws AssertionError if the actual value is not in [start, end] range.
   */
  public void assertIsBetween(AssertionInfo info, NUMBER actual, NUMBER start, NUMBER end) {
    assertIsBetween(info, actual, start, end, true, true);
  }

  /**
   * Asserts that the actual value is in ]start, end[ range (start excluded, end excluded).
   * 
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param start the start value (exclusive), expected not to be null.
   * @param end the end value (exclusive), expected not to be null.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   * @throws AssertionError if the actual value is not in ]start, end[ range.
   */
  public void assertIsStrictlyBetween(AssertionInfo info, NUMBER actual, NUMBER start, NUMBER end) {
    assertIsBetween(info, actual, start, end, false, false);
  }
}