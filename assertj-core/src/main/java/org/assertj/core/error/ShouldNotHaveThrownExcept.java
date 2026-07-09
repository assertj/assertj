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
package org.assertj.core.error;

import static org.assertj.core.util.Throwables.getStackTrace;

/** Creates errors for unexpected throwable types. */
public class ShouldNotHaveThrownExcept extends BasicErrorMessageFactory {

  /**
   * Creates an error for a throwable not among the allowed types.
   *
   * @param throwable the unexpected throwable
   * @param throwableClasses the allowed throwable types
   * @return the error message factory
   */
  @SafeVarargs
  public static ErrorMessageFactory shouldNotHaveThrownExcept(Throwable throwable,
                                                              Class<? extends Throwable>... throwableClasses) {
    return new ShouldNotHaveThrownExcept(throwable, throwableClasses);
  }

  private ShouldNotHaveThrownExcept(Throwable throwable, Class<? extends Throwable>[] throwableClasses) {
    super("%nExpecting code not to raise a throwable except%n  %s%nbut caught%n  %s",
          throwableClasses, getStackTrace(throwable));
  }
}
