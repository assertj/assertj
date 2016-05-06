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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.api;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;

public class AbstractSoftAssertions {

  protected final SoftProxies proxies;

  public AbstractSoftAssertions() {
    proxies = new SoftProxies();
  }

  protected <T, V> V proxy(Class<V> assertClass, Class<T> actualClass, T actual) {
    return proxies.create(assertClass, actualClass, actual);
  }

  protected List<Throwable> errorsCollected(){
    return proxies.errorsCollected();
  }

  protected ArrayList<String> createErrorMessagesWithLineNumbers(List<Throwable> errors) {
    ArrayList<String> errorMessages = new ArrayList<>();
    for (Throwable error : errors) {
      String errorMessage = error.getMessage();
      StackTraceElement stackTraceElement = searchForFirstUserElement(error.getStackTrace());
      if (stackTraceElement != null) {
        String className = stackTraceElement.getClassName();
        errorMessage += format("%nat %s.%s(%s.java:%s)", className, stackTraceElement.getMethodName(),
                               className.substring(className.lastIndexOf('.') + 1), stackTraceElement.getLineNumber());
      }
      errorMessages.add(errorMessage);
    }
    return errorMessages;
  }

  private StackTraceElement searchForFirstUserElement(StackTraceElement[] stacktrace) {
    for (StackTraceElement element : stacktrace) {
      String className = element.getClassName();
      if (className.startsWith("sun.reflect") || className.startsWith("java.lang.reflect")
          || className.startsWith("net.sf.cglib.proxy") || className.startsWith("org.assertj")) {
        continue;
      }
      return element;
    }
    return null;
  }
}
