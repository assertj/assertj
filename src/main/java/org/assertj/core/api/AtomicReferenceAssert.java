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


import java.util.concurrent.atomic.AtomicReference;

/**
 * Assertion methods for {@link AtomicReference}s.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(AtomicReference)}</code>.
 * </p>
 *
 * @param <VALUE> the type of object referred to by the {@link AtomicReference}.
 * @author epeee
 */
public class AtomicReferenceAssert<VALUE> extends AbstractAtomicAssert<AtomicReferenceAssert<VALUE>, VALUE, AtomicReference<VALUE>> {

  public AtomicReferenceAssert(AtomicReference<VALUE> actual) {
    super(actual, AtomicReferenceAssert.class, true);
  }

  @Override
  protected VALUE getActualValue() {
    return actual.get();
  }
}
