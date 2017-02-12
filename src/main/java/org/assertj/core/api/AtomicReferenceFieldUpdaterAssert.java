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


import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * Assertion methods for {@link AtomicReferenceFieldUpdater}s.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(AtomicReferenceFieldUpdater)}</code>.
 * </p>
 *
 * @param <FIELD> the type of the field which gets updated by the {@link AtomicReferenceFieldUpdater}.
 * @param <OBJECT> the type of the object holding the updatable field.
 * @author epeee
 * @since 2.7.0 / 3.7.0
 */
public class AtomicReferenceFieldUpdaterAssert<FIELD, OBJECT> extends AbstractAtomicFieldUpdaterAssert<AtomicReferenceFieldUpdaterAssert<FIELD, OBJECT>, FIELD, AtomicReferenceFieldUpdater<OBJECT, FIELD>, OBJECT> {

  public AtomicReferenceFieldUpdaterAssert(AtomicReferenceFieldUpdater<OBJECT, FIELD> actual) {
    super(actual, AtomicReferenceFieldUpdaterAssert.class, true);
  }

  /**
   * Verifies that the actual atomic field updater contains the given value at the given object.
   * <p>
   * Example:
   * <pre><code class='java'> // person is an instance of a Person class holding a non-private volatile String field (name).
   * AtomicReferenceFieldUpdater&lt;Person,String&gt; fieldUpdater 
   *       = AtomicReferenceFieldUpdater.newUpdater(Person.class, String.class, "name");
   * 
   * // this assertion succeeds:
   * ageUpdater.set(person, "Superman");
   * assertThat(ageUpdater).hasValue("Superman", person);
   *
   * // this assertion fails:
   * fieldUpdater.set(person, "Batman");
   * assertThat(fieldUpdater).hasValue("Superman", person);</code></pre>
   *
   * @param expectedValue the expected value inside the {@code OBJECT}.
   * @param obj the object holding the updatable field.
   * @return this assertion object.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicReferenceFieldUpdaterAssert<FIELD, OBJECT> hasValue(FIELD expectedValue, OBJECT obj) {
    return super.hasValue(expectedValue, obj);
  }
  
  @Override
  protected FIELD getActualValue(OBJECT obj) {
    return actual.get(obj);
  }
}
