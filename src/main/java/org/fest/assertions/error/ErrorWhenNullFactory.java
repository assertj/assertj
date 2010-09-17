/*
 * Created on Sep 16, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.error;

import org.fest.assertions.description.Description;
import org.fest.util.VisibleForTesting;

/**
 * Creates an <code>{@link AssertionError}</code> when an assertion that verifies that an object is not {@code null}
 * fails.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class ErrorWhenNullFactory implements AssertionErrorFactory {

  @VisibleForTesting static final AssertionErrorFactory INSTANCE = new ErrorWhenNullFactory();

  /**
   * Creates instances of <code>{@link ErrorWhenNullFactory}</code>.
   * @return an instance of {@code ErrorWhenNullFactory}.
   */
  public static AssertionErrorFactory errorWhenNull() {
    return INSTANCE;
  }

  @VisibleForTesting ErrorWhenNullFactory() {}

  /**
   * Creates an <code>{@link AssertionError}</code> when an assertion that verifies that an object is not {@code null}
   * fails.
   * @param d the description of the failed assertion.
   * @return the created {@code AssertionError}.
   */
  public AssertionError newAssertionError(Description d) {
    return new AssertionError(defaultErrorMessage(d));
  }

  private String defaultErrorMessage(Description d) {
    return Formatter.instance().formatMessage("%sexpecting non-null value but got:<null>", d);
  }
}
