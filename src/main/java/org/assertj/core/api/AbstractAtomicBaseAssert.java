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


import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;

import static org.assertj.core.error.AtomicShouldContain.shouldContain;

/**
 * Base class for all atomic assertions.
 *
 * @param <SELF> the "self" type of this assertion class.
 * @param <VALUE> the type of the "actual" value.
 * @param <ATOMIC> the type of the "actual" atomic.
 * @author epeee
 */
public abstract class AbstractAtomicBaseAssert<SELF extends AbstractAtomicBaseAssert<SELF, VALUE, ATOMIC>, VALUE, ATOMIC>
  extends AbstractAssert<SELF, ATOMIC> {

  private final ComparisonStrategy atomicValueComparisonStrategy;
  private final boolean expectedNullAllowed;

  public AbstractAtomicBaseAssert(ATOMIC actual, Class<?> selfType, boolean expectedNullAllowed) {
    super(actual, selfType);
    this.atomicValueComparisonStrategy = StandardComparisonStrategy.instance();
    this.expectedNullAllowed = expectedNullAllowed;
  }

  protected final SELF contains(VALUE expectedValue, Resolver<VALUE> valueResolver) {
    validate(expectedValue);

    VALUE resolvedValue = valueResolver.resolve();
    doAssert(resolvedValue, expectedValue);
    return myself;
  }

  protected final <T> void doAssert(T actual, T expected) {
    if (!atomicValueComparisonStrategy.areEqual(actual, expected)) {
      throwAssertionError(shouldContain(actual, expected));
    }
  }

  private void validate(VALUE expectedValue) {
    isNotNull();
    if (!expectedNullAllowed) {
      checkNotNull(expectedValue);
    }
  }

  private void checkNotNull(VALUE expectedValue) {
    if (expectedValue == null) {
      throw new IllegalArgumentException("The expected value should not be <null>.");
    }
  }

  interface Resolver<VALUE> {
    VALUE resolve();
  }

}
