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


import static org.assertj.core.error.ShouldHaveStamp.shouldHaveStamp;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * Assertion methods for {@link AtomicStampedReference}s.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(AtomicStampedReference)}</code>.
 * </p>
 *
 * @author epeee
 * @since 2.7.0 / 3.7.0
 */
public class AtomicStampedReferenceAssert<VALUE> extends AbstractAtomicReferenceAssert<AtomicStampedReferenceAssert<VALUE>, VALUE, AtomicStampedReference<VALUE>> {

  public AtomicStampedReferenceAssert(AtomicStampedReference<VALUE> actual) {
    super(actual, AtomicStampedReferenceAssert.class, true);
  }

  /**
   * Verifies that the actual {@link AtomicStampedReference} contains the given value.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicStampedReferenceAssert&lt;String&gt; ref = new AtomicStampedReferenceAssert&lt;&gt;("foo", 123);
   * 
   * // this assertion succeeds:
   * assertThat(ref).hasValue("foo");
   *
   * // this assertion fails:
   * assertThat(ref).hasValue("bar");</code></pre>
   *
   * @param expectedValue the expected value inside the {@link AtomicStampedReference}.
   * @return this assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public AtomicStampedReferenceAssert<VALUE> hasReference(VALUE expectedValue) {
    return super.hasReference(expectedValue);
  }
  
  @Override
  protected VALUE getReference() {
    return actual.getReference();
  }

  /**
   * Verifies that the actual {@link AtomicStampedReference} has the given stamp.
   *
   * Examples:
   * <pre><code class='java'> // this assertion succeeds:
   * assertThat(new AtomicStampedReference&lt;&gt;("actual", 1234)).hasStamp(1234);
   *
   * // this assertion fails:
   * assertThat(new AtomicStampedReference&lt;&gt;("actual", 1234)).hasStamp(5678);</code></pre>
   *
   * @param expectedStamp the expected stamp inside the {@link AtomicStampedReference}.
   * @return this assertion object.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicStampedReferenceAssert<VALUE> hasStamp(int expectedStamp){
    int timestamp = actual.getStamp();
    if (timestamp != expectedStamp) throwAssertionError(shouldHaveStamp(actual, expectedStamp));
    return this;
  }
}
