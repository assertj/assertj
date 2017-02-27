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


import java.time.Instant;

/**
 * Assertion methods for {@link Instant}s.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(Instant)}</code>.
 * </p>
 * @since 3.7.0
 */
public class InstantAssert extends AbstractInstantAssert<InstantAssert> {

  /**
   * Creates a new <code>{@link AbstractInstantAssert}</code>.
   *
   * @param actual   the actual value to verify
   * @since 3.7.0
   */
  public InstantAssert(Instant actual) {
    super(actual, InstantAssert.class);
  }
}
