/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/** Collects error messages of all AssertionErrors thrown by the proxied method. */
public class ErrorCollector implements MethodInterceptor {

  private static final String INTERCEPT_METHOD_NAME = "intercept";

  private static final String CLASS_NAME = ErrorCollector.class.getName();

  // scope : the current softassertion object
  private final List<Throwable> errors = new ArrayList<>();
  // scope : the last assertion call (might be nested)
  private final LastResult lastResult = new LastResult();

  @Override
  public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
    Object result = obj;
    try {
      result = proxy.invokeSuper(obj, args);
      lastResult.setSuccess(true);
    } catch (AssertionError e) {
      if (isNestedErrorCollectorProxyCall()) {
        // let the most outer call handle the assertion error
        throw e;
      }
      lastResult.setSuccess(false);
      errors.add(e);
    }
    return result;
  }

  public void addError(Throwable error) {
    errors.add(error);
    lastResult.recordError();
  }

  public List<Throwable> errors() {
    return Collections.unmodifiableList(errors);
  }

  public boolean wasSuccess() {
    return lastResult.wasSuccess();
  }

  private boolean isNestedErrorCollectorProxyCall() {
    return countErrorCollectorProxyCalls() > 1;
  }

  private static int countErrorCollectorProxyCalls() {
    int nbCalls = 0;
    for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
      if (CLASS_NAME.equals(stackTraceElement.getClassName())
          && INTERCEPT_METHOD_NAME.equals(stackTraceElement.getMethodName()))
        nbCalls++;
    }
    return nbCalls;
  }

  private static class LastResult {
    private boolean wasSuccess = true;
    private boolean errorFound = false;

    private boolean wasSuccess() {
      return wasSuccess;
    }

    private void recordError() {
      errorFound = true;
      wasSuccess = false;
    }

    private void setSuccess(boolean success) {

      // errorFound must be true if any nested call ends up in error
      // Nested call Example : softly.assertThat(true).isFalse()
      // call chain :
      // -- softly.assertThat(true).isFalse()
      // ----- proxied isFalse() -> calls isEqualTo(false) which is proxied
      // ------- proxied isEqualTo(false) : catch AssertionError => last result success = false, back to outer call
      // ---- proxied isFalse() : no AssertionError caught => last result success = true
      // The overall last result success should not be true as one of the nested calls was not a success.
      errorFound |= !success;

      if (resolvingOutermostErrorCollectorProxyNestedCall()) {
        // we are resolving the last nested call (if any), we can set a relevant value for wasSuccess
        wasSuccess = !errorFound;
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
