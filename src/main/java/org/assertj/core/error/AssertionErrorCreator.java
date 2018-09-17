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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.util.Arrays.array;

import java.util.Optional;

import org.assertj.core.util.VisibleForTesting;

// TODO deprecate AssertionErrorFactory
public class AssertionErrorCreator {

  private static final Class<?>[] MSG_ARG_TYPES_FOR_ASSERTION_FAILED_ERROR = array(String.class, Object.class, Object.class);

  @VisibleForTesting
  static ConstructorInvoker constructorInvoker = new ConstructorInvoker();

  public static AssertionError assertionError(String message, Object actual, Object expected) {
    return assertionFailedError(message, actual, expected).orElse(assertionError(message));
  }

  private static Optional<AssertionError> assertionFailedError(String message, Object actual, Object expected) {
    try {
      Object o = constructorInvoker.newInstance("org.opentest4j.AssertionFailedError",
                                                MSG_ARG_TYPES_FOR_ASSERTION_FAILED_ERROR,
                                                message,
                                                expected,
                                                actual);
      if (o instanceof AssertionError) {
        AssertionError assertionError = (AssertionError) o;
        return Optional.of(assertionError);
      }
      // TODO: single return
      return Optional.empty();
    } catch (Throwable e) {
      return Optional.empty();
    }
  }

  private static AssertionError assertionError(String message) {
    return new AssertionError(message);
  }

}
