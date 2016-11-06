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


import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * Assertion methods for {@link AtomicStampedReference}s.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(AtomicStampedReference)}</code>.
 * </p>
 *
 * @author epeee
 */
public class AtomicStampedReferenceAssert<VALUE> extends AbstractAtomicAssert<AtomicStampedReferenceAssert<VALUE>, VALUE, AtomicStampedReference<VALUE>> {

  public AtomicStampedReferenceAssert(AtomicStampedReference<VALUE> actual) {
    super(actual, AtomicStampedReferenceAssert.class, true);
  }

  @Override
  protected VALUE getActualValue() {
    return actual.getReference();
  }

  /**
   * Verifies that the actual {@link AtomicStampedReference} contains the given stamp.
   *
   * Examples:
   * <pre><code class='java'> // this assertion succeeds:
   * assertThat(new AtomicStampedReference<>("actual", 1234)).hasStamp(1234);
   *
   * // this assertion fails:
   * assertThat(new AtomicStampedReference<>("actual", 1234)).hasStamp(5678);</code></pre>
   *
   * @param expectedStamp the expected stamp inside the {@link AtomicStampedReference}.
   * @return this assertion object.
   */
  public AtomicStampedReferenceAssert<VALUE> hasStamp(int expectedStamp){
    int timestamp = actual.getStamp();
    doAssert(timestamp, expectedStamp);
    return this;
  }
}
