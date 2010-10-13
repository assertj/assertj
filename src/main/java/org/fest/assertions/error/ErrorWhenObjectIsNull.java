/*
 * Created on Sep 21, 2010
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

/**
 * Creates an <code>{@link AssertionError}</code> when an assertion that verifies that an object is not {@code null}
 * fails.
 *
 * @author Alex Ruiz
 */
public class ErrorWhenObjectIsNull implements AssertionErrorFactory {

  public static final ErrorWhenObjectIsNull INSTANCE = new ErrorWhenObjectIsNull();

  /**
   * Creates instances of <code>{@link ErrorWhenObjectIsNull}</code>.
   * @return an instance of {@code ErrorWhenObjectIsNull}.
   */
  public static AssertionErrorFactory errorWhenNull() {
    return INSTANCE;
  }

  private ErrorWhenObjectIsNull() {}

  /**
   * Creates an <code>{@link AssertionError}</code> when an assertion that verifies that an object is not {@code null}
   * fails.
   * @param d the description of the failed assertion.
   * @return the created {@code AssertionError}.
   */
  public AssertionError newAssertionError(Description d) {
    String message = Formatter.instance().formatMessage("%sexpecting non-null object but it was null", d);
    return new AssertionError(message);
  }
}
