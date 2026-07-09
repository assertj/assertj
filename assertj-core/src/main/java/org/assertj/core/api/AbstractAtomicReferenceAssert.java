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

import static org.assertj.core.error.ShouldHaveReference.shouldHaveReference;

/**
 * Base class for atomic assertions.
 *
 * @param <SELF> the "self" type of this assertion class.
 * @param <VALUE> the type of the "actual" value.
 * @param <ATOMIC> the type of the "actual" atomic.
 * @author epeee
 * @since 2.7.0 / 3.7.0
 */
public abstract class AbstractAtomicReferenceAssert<SELF extends AbstractAtomicReferenceAssert<SELF, VALUE, ATOMIC>, VALUE, ATOMIC>
    extends AbstractObjectAssert<SELF, ATOMIC> {

  /**
   * Creates a new atomic reference assertion.
   *
   * @param actual the actual atomic reference
   * @param selfType the type of the concrete assertion
   */
  protected AbstractAtomicReferenceAssert(ATOMIC actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the atomic object holds the expected reference.
   *
   * @param expectedReference the expected reference
   * @return this assertion object
   */
  public SELF hasReference(VALUE expectedReference) {
    return executeAssertion(() -> {
      isNotNull();
      if (!this.objects.getComparisonStrategy().areEqual(getReference(), expectedReference)) {
        throwAssertionError(shouldHaveReference(actual, getReference(), expectedReference));
      }
    });
  }

  /**
   * Returns the reference held by the actual atomic object.
   *
   * @return the held reference
   */
  protected abstract VALUE getReference();

}
