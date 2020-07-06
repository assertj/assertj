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
package org.assertj.core.api;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import net.bytebuddy.implementation.bind.annotation.FieldValue;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.StubValue;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.SuperMethod;
import net.bytebuddy.implementation.bind.annotation.This;

/** Collects error messages of all AssertionErrors thrown by the proxied method. */
public class ErrorCollector {

  public static final String FIELD_NAME = "errorCollector";

  private static final String INTERCEPT_METHOD_NAME = "intercept";
  private static final String CLASS_NAME = ErrorCollector.class.getName();

  // The list is synchronized in case errors arrive from more than a single thread.
  // scope : the current softassertion object
  private final List<AssertionError> errors = Collections.synchronizedList(new ArrayList<>());
  // scope : the last assertion call (might be nested)
  private final LastResult lastResult = new LastResult();

  private AfterAssertionErrorCollected afterAssertionErrorCollected;

  void setAfterAssertionErrorCollected(AfterAssertionErrorCollected afterAssertionErrorCollected) {
    this.afterAssertionErrorCollected = afterAssertionErrorCollected;
  }

  /**
   * @param errorCollector the {@link ErrorCollector} to gather assertions error for the assertion instance
   * @param assertion The instance of the method, the this reference.
   * @param proxy A proxy to invoke the original method.
   * @param method A reference to the original method.
   * @param stub A default value for the return type. null for reference type and 0 for the corresponding primitive types.
   * @return the assertion result
   * @throws Exception may be thrown from the assertion proxy call
   */
  @RuntimeType
  public static Object intercept(@FieldValue(FIELD_NAME) ErrorCollector errorCollector,
                                 @This Object assertion,
                                 @SuperCall Callable<?> proxy,
                                 @SuperMethod(nullIfImpossible = true) Method method,
                                 @StubValue Object stub) throws Exception {
    try {
      Object result = proxy.call();
      errorCollector.lastResult.setSuccess(true);
      return result;
    } catch (AssertionError assertionError) {
      if (errorCollector.isNestedErrorCollectorProxyCall()) {
        // let the most outer call handle the assertion error
        throw assertionError;
      }
      errorCollector.addError(assertionError);
    }
    if (method != null && !method.getReturnType().isInstance(assertion)) {
      // In case the object is not an instance of the return type, just default value for the return type:
      // null for reference type and 0 for the corresponding primitive types.
      return stub;
    }
    return assertion;
  }

  void addError(AssertionError error) {
    errors.add(error);
    lastResult.setSuccess(false);
    if (afterAssertionErrorCollected != null) {
      afterAssertionErrorCollected.onAssertionErrorCollected(error);
    }
  }

  public List<AssertionError> errors() {
    return Collections.unmodifiableList(errors);
  }

  public boolean wasSuccess() {
    return lastResult.wasSuccess();
  }

  private boolean isNestedErrorCollectorProxyCall() {
    return countErrorCollectorProxyCalls() > 1;
  }

  private static long countErrorCollectorProxyCalls() {
    return Arrays.stream(Thread.currentThread().getStackTrace())
                 .filter(stackTraceElement -> CLASS_NAME.equals(stackTraceElement.getClassName())
                                              && stackTraceElement.getMethodName().startsWith(INTERCEPT_METHOD_NAME))
                 .count();
  }

  private static class LastResult {
    // Marking these fields as volatile doesn't ensure complete thread safety
    // (mutual exclusion, race-free behaviour), but guarantees eventual visibility
    private volatile boolean wasSuccess = true;
    private volatile boolean errorFound = false;

    private boolean wasSuccess() {
      return wasSuccess;
    }

    private void setSuccess(boolean success) {

      // errorFound must be true if any nested call ends up in error
      // Nested call Example : softly.assertThat(true).isFalse()
      // call chain :
      // -- softly.assertThat(true).isFalse()
      // ----- proxied isFalse() -> calls isEqualTo(false) which is proxied
      // ------- proxied isEqualTo(false) : catch AssertionError => last result success = false, back to outer call
      // ---- proxied isFalse() : no AssertionError caught => last result success = true
      errorFound |= !success;
      wasSuccess = success;

      if (resolvingOutermostErrorCollectorProxyNestedCall()) {
        // need to reset errorFound for the next soft assertion
        errorFound = false;
      }
    }

    private boolean resolvingOutermostErrorCollectorProxyNestedCall() {
      return countErrorCollectorProxyCalls() == 1;
    }

    @Override
    public String toString() {
      return String.format("LastResult [wasSuccess=%s, errorFound=%s]", wasSuccess, errorFound);
    }

  }
}
