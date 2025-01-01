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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api;

import java.lang.reflect.Method;
import java.util.Arrays;
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

  private AssertionErrorCollector assertionErrorCollector;

  ErrorCollector(AssertionErrorCollector collector) {
    this.assertionErrorCollector = collector;
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
      errorCollector.succeeded();
      return result;
    } catch (AssertionError assertionError) {
      if (isNestedErrorCollectorProxyCall()) {
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

  private void addError(AssertionError error) {
    assertionErrorCollector.collectAssertionError(error);
  }

  private void succeeded() {
    assertionErrorCollector.succeeded();
  }

  private static boolean isNestedErrorCollectorProxyCall() {
    return countErrorCollectorProxyCalls() > 1;
  }

  private static long countErrorCollectorProxyCalls() {
    return Arrays.stream(Thread.currentThread().getStackTrace())
                 .filter(stackTraceElement -> CLASS_NAME.equals(stackTraceElement.getClassName())
                                              && stackTraceElement.getMethodName().startsWith(INTERCEPT_METHOD_NAME))
                 .count();
  }
}
