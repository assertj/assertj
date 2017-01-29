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
package org.assertj.core.internal;

import static org.assertj.core.util.Preconditions.checkNotNull;

import java.util.function.Predicate;

import org.assertj.core.util.VisibleForTesting;

public class Predicates {

  private static final Predicates INSTANCE = new Predicates();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static Predicates instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Failures failures = Failures.instance();

  @VisibleForTesting
  Predicates() {}

  /**
   * Asserts the the given <code>{@link Predicate}</code> is not null.
   * @param predicate the given {@code Predicate}.
   * @throws NullPointerException if the given {@code Predicate} is {@code null}.
   */
  public void assertIsNotNull(Predicate<?> predicate) {
    checkNotNull(predicate, "The predicate to evaluate should not be null");
  }
}
