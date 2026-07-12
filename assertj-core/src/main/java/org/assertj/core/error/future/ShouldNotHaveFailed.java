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

import java.util.concurrent.CompletableFuture;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

/** Creates errors for futures expected not to fail. */
public class ShouldNotHaveFailed extends BasicErrorMessageFactory {

  private static final String SHOULD_NOT_HAVE_FAILED = "%nExpecting%n  <%s>%nto not have failed.%n" + Warning.WARNING;

  /**
   * Creates an error for a future expected not to fail.
   *
   * @param actual the actual future
   * @return the error message factory
   */
  public static ErrorMessageFactory shouldNotHaveFailed(CompletableFuture<?> actual) {
    return new ShouldNotHaveFailed(actual);
  }

  private ShouldNotHaveFailed(CompletableFuture<?> actual) {
    super(SHOULD_NOT_HAVE_FAILED, actual);
  }
}
