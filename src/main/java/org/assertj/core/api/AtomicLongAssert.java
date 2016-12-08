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


import java.util.concurrent.atomic.AtomicLong;

/**
 * Assertion methods for {@link AtomicLong}s.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(AtomicLong)}</code>.
 * </p>
 *
 * @author epeee
 */
public class AtomicLongAssert extends AbstractAtomicAssert<AtomicLongAssert, Long, AtomicLong> {

  public AtomicLongAssert(AtomicLong actual) {
    super(actual, AtomicLongAssert.class, false);
  }

  @Override
  protected Long getActualValue() {
    return actual.get();
  }
}
