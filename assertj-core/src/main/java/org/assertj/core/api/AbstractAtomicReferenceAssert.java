/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.error.ShouldHaveReference.shouldHaveReference;

/**
 * Base class for atomic assertions.
 *
 * @author epeee
 *
 * @param <SELF> the "self" type of this assertion class.
 * @param <VALUE> the type of the "actual" value.
 * @param <ATOMIC> the type of the "actual" atomic.
 * @since 2.7.0 / 3.7.0
 */
public abstract class AbstractAtomicReferenceAssert<SELF extends AbstractAtomicReferenceAssert<SELF, VALUE, ATOMIC>, VALUE, ATOMIC>
    extends AbstractObjectAssert<SELF, ATOMIC> {

  protected AbstractAtomicReferenceAssert(ATOMIC actual, Class<?> selfType) {
    super(actual, selfType);
  }

  public SELF hasReference(VALUE expectedReference) {
    isNotNull();
    if (!this.objects.getComparisonStrategy().areEqual(getReference(), expectedReference)) {
      throwAssertionError(shouldHaveReference(actual, getReference(), expectedReference));
    }
    return myself;
  }

  protected abstract VALUE getReference();

}
