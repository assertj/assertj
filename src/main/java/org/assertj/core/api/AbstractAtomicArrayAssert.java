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

/**
 * Base class for all array-based atomic assertions.
 *
 * @param <SELF> the "self" type of this assertion class.
 * @param <VALUE> the type of the "actual" value.
 * @param <ATOMIC> the type of the "actual" atomic.
 * @author epeee
 */
public abstract class AbstractAtomicArrayAssert<SELF extends AbstractAtomicArrayAssert<SELF,VALUE,ATOMIC>,VALUE,ATOMIC>
  extends AbstractAtomicBaseAssert<SELF,VALUE,ATOMIC> {

  public AbstractAtomicArrayAssert(ATOMIC actual, Class<?> selfType, boolean expectedNullAllowed) {
    super(actual, selfType, expectedNullAllowed);
  }

  /**
   * Verifies that the actual {@link SELF} contains the given value at the given index.
   *
   * @param expectedValue the expected value inside the {@link SELF}.
   * @param index the index where the value should be stored.
   * @return this assertion object.
   */
  public SELF hasValue(VALUE expectedValue, final int index) {
    return contains(expectedValue, new Resolver<VALUE>() {
      @Override
      public VALUE resolve() {
        return getActualValue(index);
      }
    });
  }

  protected abstract VALUE getActualValue(int index);

}

