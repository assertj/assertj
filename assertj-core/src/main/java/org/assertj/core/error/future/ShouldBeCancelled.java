/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.error.future;

import java.util.concurrent.Future;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

/** Creates errors for futures expected to be cancelled. */
public class ShouldBeCancelled extends BasicErrorMessageFactory {

  private static final String SHOULD_BE_CANCELLED = "%nExpecting%n  <%s>%nto be cancelled.%n" + Warning.WARNING;

  /**
   * Creates an error for a future expected to be cancelled.
   *
   * @param actual the actual future
   * @return the error message factory
   */
  public static ErrorMessageFactory shouldBeCancelled(Future<?> actual) {
    return new ShouldBeCancelled(actual);
  }

  private ShouldBeCancelled(Future<?> actual) {
    super(SHOULD_BE_CANCELLED, actual);
  }
}
