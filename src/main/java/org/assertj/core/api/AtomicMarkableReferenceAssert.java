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


import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * Assertion methods for {@link AtomicMarkableReference}s.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(AtomicMarkableReference)}</code>.
 * </p>
 *
 * @author epeee
 */
public class AtomicMarkableReferenceAssert<VALUE> extends AbstractAtomicAssert<AtomicMarkableReferenceAssert<VALUE>, VALUE, AtomicMarkableReference<VALUE>> {

  public AtomicMarkableReferenceAssert(AtomicMarkableReference<VALUE> actual) {
    super(actual, AtomicMarkableReferenceAssert.class, true);
  }

  @Override
  protected VALUE getActualValue() {
    return actual.getReference();
  }

  /**
   * Verifies that the actual {@link AtomicMarkableReference} contains the given marked value.
   *
   * @param expected the expected marked inside the {@link AtomicMarkableReference}.
   * @return this assertion object.
   */
  public AtomicMarkableReferenceAssert<VALUE> isMarked(boolean expected) {
    boolean marked = actual.isMarked();
    doAssert(marked, expected);
    return this;
  }
}
