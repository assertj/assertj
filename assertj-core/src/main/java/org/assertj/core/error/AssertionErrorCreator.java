/*
 * Copyright 2012-2025 the original author or authors.
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

import static org.assertj.core.util.Arrays.array;

import java.util.List;
import java.util.Optional;

import org.assertj.core.description.Description;
import org.assertj.core.internal.Failures;
import org.assertj.core.presentation.Representation;

public class AssertionErrorCreator {

  private static final Class<?>[] MSG_ARG_TYPES_FOR_ASSERTION_FAILED_ERROR = array(String.class, Object.class, Object.class);

  // TODO reduce the visibility of the fields annotated with @VisibleForTesting
  ConstructorInvoker constructorInvoker;

  public AssertionErrorCreator() {
    this(new ConstructorInvoker());
  }

  public AssertionErrorCreator(ConstructorInvoker constructorInvoker) {
    this.constructorInvoker = constructorInvoker;
  }

  // single assertion error

  public AssertionError assertionError(String message, Object actual, Object expected, Representation representation) {
    return assertionFailedError(message, actual, expected).orElse(assertionError(message));
  }

  private Optional<AssertionError> assertionFailedError(String message, Object actual, Object expected) {
    try {
      // TODO use UnambiguousRepresentation unambiguousRepresentation = new UnambiguousRepresentation(representation, actual,
      // expected);
      Object o = constructorInvoker.newInstance("org.opentest4j.AssertionFailedError",
                                                MSG_ARG_TYPES_FOR_ASSERTION_FAILED_ERROR,
                                                message,
                                                expected,
                                                actual);

      if (o instanceof AssertionError error) return Optional.of(error);

    } catch (@SuppressWarnings("unused") Throwable ignored) {}
    return Optional.empty();
  }

  public AssertionError assertionError(String message) {
    return new AssertionError(message);
  }

  // multiple assertions error

  public AssertionError multipleAssertionsError(List<AssertionError> errors) {
    return multipleAssertionsError(null, null, errors);
  }

  public AssertionError multipleAssertionsError(Description description, Object objectUnderTest, List<AssertionError> errors) {
    MultipleAssertionsError multipleAssertionsError = new MultipleAssertionsError(description, objectUnderTest, errors);
    Failures.instance().removeAssertJRelatedElementsFromStackTraceIfNeeded(multipleAssertionsError);
    return multipleAssertionsError;
  }
}
