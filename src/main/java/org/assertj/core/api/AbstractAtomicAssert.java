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
 * Base class for atomic assertions.
 *
 * @param <SELF> the "self" type of this assertion class.
 * @param <VALUE> the type of the "actual" value.
 * @param <ATOMIC> the type of the "actual" atomic.
 * @author epeee
 */
public abstract class AbstractAtomicAssert<SELF extends AbstractAtomicAssert<SELF, VALUE, ATOMIC>, VALUE, ATOMIC>
    extends AbstractAtomicBaseAssert<SELF, VALUE, ATOMIC> {

  public AbstractAtomicAssert(ATOMIC actual, Class<?> selfType, boolean expectedNullAllowed) {
    super(actual, selfType, expectedNullAllowed);
  }

  /**
   * Verifies that the actual {@link SELF} contains the given value.
   *
   * Examples:
   * <pre><code class='java'> // this assertion succeeds:
   * assertThat(new AtomicInteger(1234)).hasValue(1234);
   *
   * // this assertion fails:
   * assertThat(new AtomicInteger(1234)).hasValue(5678);</code></pre>
   *
   * @param expectedValue the expected value inside the {@link SELF}.
   * @return this assertion object.
   */
  public SELF hasValue(VALUE expectedValue) {
    return contains(expectedValue, new Supplier<VALUE>() {
      @Override
      public VALUE get() {
        return getActualValue();
      }
    });
  }

  protected abstract VALUE getActualValue();

}
