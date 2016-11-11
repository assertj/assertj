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
 * Base class for all fieldupdater assertions.
 *
 * @param <SELF> the "self" type of this assertion class.
 * @param <VALUE> the type of the "actual" value.
 * @param <ATOMIC> the type of the "actual" atomic.
 * @param <OBJECT> the type of the object holding the updatable field.
 * @author epeee
 */
public abstract class AbstractAtomicFieldUpdaterAssert<SELF extends AbstractAtomicFieldUpdaterAssert<SELF, VALUE, ATOMIC, OBJECT>, VALUE, ATOMIC, OBJECT>
    extends AbstractAtomicBaseAssert<SELF, VALUE, ATOMIC> {

  public AbstractAtomicFieldUpdaterAssert(ATOMIC actual, Class<?> selfType, boolean expectedNullAllowed) {
    super(actual, selfType, expectedNullAllowed);
  }

  /**
   * Verifies that the actual atomic field updater contains the given value at the given object.
   * <p>
   * Example with {@code AtomicIntegerFieldUpdater}:
   * <pre><code class='java'> // person is an instance of a Person class holding a non-private volatile int field (age).
   * AtomicIntegerFieldUpdater&lt;Person&gt; ageUpdater = AtomicIntegerFieldUpdater.newUpdater(Person.class, "age");
   * 
   * // this assertion succeeds:
   * ageUpdater.set(person, 25);
   * assertThat(ageUpdater).hasValue(25, person);
   *
   * // this assertion fails:
   * fieldUpdater.set(person, 28);
   * assertThat(fieldUpdater).hasValue(25, person);</code></pre>
   *
   * @param expectedValue the expected value inside the {@link SELF}.
   * @param obj the object holding the updatable field.
   * @return this assertion object.
   */
  public SELF hasValue(VALUE expectedValue, final OBJECT obj) {
    return contains(expectedValue, new Supplier<VALUE>() {
      @Override
      public VALUE get() {
        return getActualValue(obj);
      }
    });
  }

  protected abstract VALUE getActualValue(OBJECT obj);

}
