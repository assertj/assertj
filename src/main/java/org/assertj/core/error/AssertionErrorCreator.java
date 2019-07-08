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
package org.assertj.core.error;

import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Throwables.describeErrors;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.assertj.core.api.SoftAssertionError;
import org.assertj.core.description.Description;
import org.assertj.core.internal.Failures;
import org.assertj.core.util.introspection.PropertyOrFieldSupport;

// TODO deprecate AssertionErrorFactory
public class AssertionErrorCreator {

  private static final Class<?>[] MSG_ARG_TYPES_FOR_ASSERTION_FAILED_ERROR = array(String.class, Object.class, Object.class);

  private static final Class<?>[] MULTIPLE_FAILURES_ERROR_ARGUMENT_TYPES = array(String.class, List.class);

  private static final Class<?>[] MSG_WITH_REASON_ARGS_FOR_ASSERTION_FAILED_ERROR = array(String.class, Throwable.class);

  private static final Class<?>[] MSG_ONLY_ARGS_FOR_ASSERTION_FAILED_ERROR = array(String.class);

  private ConstructorInvoker constructorInvoker;

  public AssertionErrorCreator() {
    this(new ConstructorInvoker());
  }

  public AssertionErrorCreator(ConstructorInvoker constructorInvoker) {
    this.constructorInvoker = constructorInvoker;
  }

  // single assertion error

  public AssertionError assertionError(String message, Object actual, Object expected) {
    return new LazyAssertionFailedErrorCreator(message, actual, expected).assertionErrorIfCantBeCreated();
  }

  public AssertionError assertionError(String message, Throwable reason) {
    return new LazyAssertionFailedErrorCreator(message, reason).assertionErrorIfCantBeCreated();
  }

  public AssertionError assertionError(String message) {
    return new LazyAssertionFailedErrorCreator(message).assertionErrorIfCantBeCreated();
  }

  private class LazyAssertionFailedErrorCreator {
    private final Supplier<Object> supplier;
    private final String message;

    LazyAssertionFailedErrorCreator(String message, Object actual, Object expected) {
      this.supplier = () -> tryInstantiateAssertionFailedError(message, actual, expected);
      this.message = message;
    }

    LazyAssertionFailedErrorCreator(String message, Throwable reason) {
      this.supplier = () -> tryInstantiateAssertionFailedError(message, reason);
      this.message = message;
    }

    LazyAssertionFailedErrorCreator(String message) {
      this.supplier = () -> tryInstantiateAssertionFailedError(message);
      this.message = message;
    }

    AssertionError assertionErrorIfCantBeCreated() {
      return Optional.ofNullable(supplier.get())
        .filter(AssertionError.class::isInstance)
        .map(AssertionError.class::cast)
        .map(this::removeStackTraceIfNeeded)
        .orElse(new AssertionError(message));
    }

    private AssertionError removeStackTraceIfNeeded(AssertionError e) {
      Failures.instance().removeAssertJRelatedElementsFromStackTraceIfNeeded(e);
      return e;
    }
  }

  private Object tryInstantiateAssertionFailedError(String message) {
    try {
      return constructorInvoker.newInstance("org.opentest4j.AssertionFailedError",
                                            MSG_ONLY_ARGS_FOR_ASSERTION_FAILED_ERROR,
                                            message);
    } catch (Throwable e) {
      return null;
    }
  }

  private Object tryInstantiateAssertionFailedError(String message, Object actual, Object expected) {
    try {
      return constructorInvoker.newInstance("org.opentest4j.AssertionFailedError",
                                            MSG_ARG_TYPES_FOR_ASSERTION_FAILED_ERROR,
                                            message,
                                            expected,
                                            actual);
    } catch (Throwable e) {
      return null;
    }
  }

  private Object tryInstantiateAssertionFailedError(String message, Throwable reason) {
    try {
      return constructorInvoker.newInstance("org.opentest4j.AssertionFailedError",
                                            MSG_WITH_REASON_ARGS_FOR_ASSERTION_FAILED_ERROR,
                                            message,
                                            reason);
    } catch (Throwable e) {
      return null;
    }
  }

  // multiple assertions error

  public AssertionError multipleSoftAssertionsError(List<? extends Throwable> errors) {
    Optional<AssertionError> multipleFailuresError = tryBuildingMultipleFailuresError(errors);
    return multipleFailuresError.orElse(new SoftAssertionError(describeErrors(errors)));
  }

  public AssertionError multipleAssertionsError(Description description, List<? extends AssertionError> errors) {
    String heading = headingFrom(description);
    Optional<AssertionError> multipleFailuresError = tryBuildingMultipleFailuresError(heading, errors);
    return multipleFailuresError.orElse(new MultipleAssertionsError(description, errors));
  }

  public void tryThrowingMultipleFailuresError(List<? extends Throwable> errorsCollected) {
    tryBuildingMultipleFailuresError(errorsCollected).ifPresent(AssertionErrorCreator::throwError);
  }

  // syntactic sugar
  private static void throwError(AssertionError error) {
    throw error;
  }

  private static String headingFrom(Description description) {
    return description == null ? null : DescriptionFormatter.instance().format(description);
  }

  private Optional<AssertionError> tryBuildingMultipleFailuresError(List<? extends Throwable> errorsCollected) {
    return tryBuildingMultipleFailuresError(null, errorsCollected);
  }

  private Optional<AssertionError> tryBuildingMultipleFailuresError(String heading,
                                                                    List<? extends Throwable> errorsCollected) {
    if (errorsCollected.isEmpty()) return Optional.empty();
    try {
      Object[] constructorArguments = array(heading, errorsCollected);
      Object multipleFailuresError = constructorInvoker.newInstance("org.opentest4j.MultipleFailuresError",
                                                                    MULTIPLE_FAILURES_ERROR_ARGUMENT_TYPES,
                                                                    constructorArguments);
      if (multipleFailuresError instanceof AssertionError) { // means that we were able to build a MultipleFailuresError
        List<Throwable> failures = extractFailuresOf(multipleFailuresError);
        // we switch to AssertJMultipleFailuresError in order to control the formatting of the error message.
        // we use reflection to avoid making opentest4j a required dependency
        AssertionError assertionError = (AssertionError) constructorInvoker.newInstance("org.assertj.core.error.AssertJMultipleFailuresError",
                                                                                        MULTIPLE_FAILURES_ERROR_ARGUMENT_TYPES,
                                                                                        array(heading, failures));
        Failures.instance().removeAssertJRelatedElementsFromStackTraceIfNeeded(assertionError);
        return Optional.of(assertionError);
      }
    } catch (Exception e) {
      // do nothing, MultipleFailuresError was not in the classpath
    }
    return Optional.empty();
  }

  @SuppressWarnings("unchecked")
  private static List<Throwable> extractFailuresOf(Object multipleFailuresError) {
    return (List<Throwable>) PropertyOrFieldSupport.EXTRACTION.getValueOf("failures", multipleFailuresError);
  }

}
