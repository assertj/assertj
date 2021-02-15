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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.internal;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.VisibleForTesting;

import static org.assertj.core.api.Fail.failBecauseExceptionWasNotThrown;

/**
 * Reusable assertions for <code>{@link java.util.Collection}</code>
 */
public class Collection {

  private static final Collection INSTANCE = new Collection();

  @VisibleForTesting
  Failures failures = Failures.instance();

  /**
   * Returns singleton instance of {@link java.util.Collection}
   * @return the singleton instance of {@link Collection}
   */
  public static Collection instance() {
    return INSTANCE;
  }

  public void assertIsUnmodifiable(AssertionInfo info, java.util.Collection<?> actual) {
    assertNotNull(info, actual);
    assertUnsupportedOperationExceptionIsThrown(info, actual.getClass(), () -> actual.add(null));
  }

  private void assertUnsupportedOperationExceptionIsThrown(AssertionInfo info, Class<?> clazz, Runnable runnable) {
    try {
      runnable.run();
    } catch (Exception ex) {
      if (ex instanceof UnsupportedOperationException) {
        return;
      }
    }
    failBecauseExceptionWasNotThrown(UnsupportedOperationException.class);
  }

  private void assertNotNull(AssertionInfo info, java.util.Collection<?> actual) {
    Objects.instance().assertNotNull(info, actual);
  }

}
