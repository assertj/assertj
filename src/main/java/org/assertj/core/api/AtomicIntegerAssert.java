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
package org.assertj.core.api;


import org.assertj.core.data.Percentage;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.error.ShouldHaveValue.shouldHaveValue;

public class AtomicIntegerAssert extends AbstractAssert<AtomicIntegerAssert, AtomicInteger> {

  private final IntegerAssert integerAssert;

  public AtomicIntegerAssert(AtomicInteger actual) {
    super(actual, AtomicIntegerAssert.class);
    this.integerAssert = new IntegerAssert(actual == null ? null : actual.get());
  }

  public AtomicIntegerAssert isBetween(int start, int end){
    integerAssert.isBetween(start, end);
    return myself;
  }

  public AtomicIntegerAssert isLessThan(int other) {
    integerAssert.isLessThan(other);
    return myself;
  }

  public AtomicIntegerAssert isGreaterThan(int other) {
    integerAssert.isGreaterThan(other);
    return myself;
  }

  public AtomicIntegerAssert isPositive() {
    integerAssert.isPositive();
    return myself;
  }

  public AtomicIntegerAssert isNotPositive() {
    integerAssert.isNotPositive();
    return myself;
  }

  public AtomicIntegerAssert isNegative() {
    integerAssert.isNegative();
    return myself;
  }

  public AtomicIntegerAssert isNotNegative() {
    integerAssert.isNotNegative();
    return myself;
  }

  public AtomicIntegerAssert isCloseTo(int expected, Percentage percentage) {
    integerAssert.isCloseTo(expected, percentage);
    return myself;
  }

  public AtomicIntegerAssert hasValue(int expectedValue) {
    int actualValue = actual.get();
    if(!objects.getComparisonStrategy().areEqual(actualValue, expectedValue)) {
      throwAssertionError(shouldHaveValue(actual, expectedValue));
    }
    return myself;
  }
}
