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
package org.assertj.core.api;

import static org.assertj.core.error.ShouldHaveValue.shouldHaveValue;
import static org.assertj.core.util.Preconditions.checkArgument;

/**
 * Base class for all fieldupdater assertions.
 *
 * @param <SELF> the "self" type of this assertion class.
 * @param <VALUE> the type of the "actual" value.
 * @param <ATOMIC> the type of the "actual" atomic.
 * @param <OBJECT> the type of the object holding the updatable field.
 * @author epeee
 * @since 2.7.0 / 3.7.0
 */
public abstract class AbstractAtomicFieldUpdaterAssert<SELF extends AbstractAtomicFieldUpdaterAssert<SELF, VALUE, ATOMIC, OBJECT>, VALUE, ATOMIC, OBJECT>
    extends AbstractObjectAssert<SELF, ATOMIC> {

  private final boolean expectedNullAllowed;

  public AbstractAtomicFieldUpdaterAssert(ATOMIC actual, Class<?> selfType, boolean expectedNullAllowed) {
    super(actual, selfType);
    this.expectedNullAllowed = expectedNullAllowed;
  }

  public SELF hasValue(VALUE expectedValue, final OBJECT obj) {
    validate(expectedValue);
    VALUE actualValue = getActualValue(obj);
    if (!objects.getComparisonStrategy().areEqual(actualValue, expectedValue)) {
      throwAssertionError(shouldHaveValue(actual, actualValue, expectedValue, obj));
    }
    return myself;
  }

  protected abstract VALUE getActualValue(OBJECT obj);

  protected void validate(VALUE expectedValue) {
    isNotNull();
    if (!expectedNullAllowed) {
      checkNotNull(expectedValue);
    }
  }

  private void checkNotNull(VALUE expectedValue) {
    checkArgument(expectedValue != null, "The expected value should not be <null>.");
  }

}
