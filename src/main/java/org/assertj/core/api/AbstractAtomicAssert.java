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


public abstract class AbstractAtomicAssert<S extends AbstractAtomicAssert<S,T,U>,T,U> extends AbstractAssert<S, U> {

  private ComparisonStrategy abstractAtomicValueComparisonStrategy;

  public AbstractAtomicAssert(U actual, Class<?> selfType) {
    super(actual, selfType);
    this.abstractAtomicValueComparisonStrategy = StandardComparisonStrategy.instance();
  }

  public S hasValue(T expectedValue) {
    return contains(expectedValue);
  }

  protected abstract T getActualValue();

  private S contains(T expectedValue) {
    isNotNull();
    checkNotNull(expectedValue);
    if (!abstractAtomicValueComparisonStrategy.areEqual(getActualValue(), expectedValue))
      throwAssertionError(shouldContain(getActualValue(), expectedValue));
    return myself;
  }

  private void checkNotNull(T expectedValue) {
    if (expectedValue == null) throw new IllegalArgumentException("The expected value should not be <null>.");
  }

}
