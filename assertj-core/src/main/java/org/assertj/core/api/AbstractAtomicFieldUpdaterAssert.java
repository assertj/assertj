/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

  /**
   * Creates a new atomic field updater assertion.
   *
   * @param actual the actual atomic field updater
   * @param selfType the type of the concrete assertion
   * @param expectedNullAllowed whether {@code null} is an allowed expected value
   */
  protected AbstractAtomicFieldUpdaterAssert(ATOMIC actual, Class<?> selfType, boolean expectedNullAllowed) {
    super(actual, selfType);
    this.expectedNullAllowed = expectedNullAllowed;
  }

  /**
   * Verifies that the updated field of the given object has the expected value.
   *
   * @param expectedValue the expected field value
   * @param obj the object holding the updated field
   * @return this assertion object
   */
  public SELF hasValue(VALUE expectedValue, final OBJECT obj) {
    return executeAssertion(() -> {
      validate(expectedValue);
      VALUE actualValue = getActualValue(obj);
      if (!objects.getComparisonStrategy().areEqual(actualValue, expectedValue)) {
        throwAssertionError(shouldHaveValue(actual, actualValue, expectedValue, obj));
      }
    });
  }

  /**
   * Returns the current value of the updated field in the given object.
   *
   * @param obj the object holding the updated field
   * @return the current field value
   */
  protected abstract VALUE getActualValue(OBJECT obj);

  /**
   * Validates the expected value.
   *
   * @param expectedValue the expected value to validate
   */
  protected void validate(VALUE expectedValue) {
    isNotNull();
    if (!expectedNullAllowed) {
      checkArgument(expectedValue != null, "The expected value should not be <null>.");
    }
  }

}
