/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.internal;

import static org.assertj.core.error.ShouldBeExhausted.shouldBeExhausted;
import static org.assertj.core.error.ShouldHaveNext.shouldHaveNext;
import static org.assertj.core.internal.Comparables.assertNotNull;

import java.util.Iterator;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link Iterator}</code>s.
 *
 * @author Stephan Windmüller
 */
public class Iterators {

  private static final Iterators INSTANCE = new Iterators();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static Iterators instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Failures failures = Failures.instance();

  @VisibleForTesting
  Iterators() {}

  public void assertHasNext(AssertionInfo info, Iterator<?> actual) {
    assertNotNull(info, actual);
    if (!actual.hasNext()) throw failures.failure(info, shouldHaveNext());
  }

  public void assertIsExhausted(AssertionInfo info, Iterator<?> actual) {
    assertNotNull(info, actual);
    if (actual.hasNext()) throw failures.failure(info, shouldBeExhausted());
  }

}
