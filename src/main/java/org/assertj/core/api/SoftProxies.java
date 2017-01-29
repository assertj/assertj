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

import static org.assertj.core.util.Arrays.array;

import java.lang.reflect.Method;
import java.util.List;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;

class SoftProxies {

  private final ErrorCollector collector = new ErrorCollector();

  void collectError(Throwable error) {
    collector.addError(error);
  }

  List<Throwable> errorsCollected() {
    return collector.errors();
  }

  @SuppressWarnings("unchecked")
  <V, T> V create(Class<V> assertClass, Class<T> actualClass, T actual) {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(assertClass);
    enhancer.setCallbackFilter(CollectErrorsOrCreateExtractedProxy.FILTER);
    enhancer.setCallbacks(new Callback[] { collector, new ProxifyExtractingResult(this) });
    return (V) enhancer.create(array(actualClass), array(actual));
  }

  public boolean wasSuccess() {
    return collector.wasSuccess();
  }

  private enum CollectErrorsOrCreateExtractedProxy implements CallbackFilter {
    FILTER;

    private static final int ERROR_COLLECTOR_INDEX = 0;
    private static final int PROXIFY_RESULT_INDEX = 1;

    @Override
    public int accept(Method method) {
      return keepProxifying(method) ? PROXIFY_RESULT_INDEX : ERROR_COLLECTOR_INDEX;
    }

    private boolean keepProxifying(Method method) {
      return isExtractingMethod(method)
             || isFilteredOnMethod(method)
             || isOptionalAssertFlatMap(method)
             || isOptionalAssertMap(method);
    }

    private boolean isExtractingMethod(Method method) {
      return method.getName().toLowerCase().contains("extracting");
    }

    private boolean isFilteredOnMethod(Method method) {
      return method.getName().contains("filteredOn");
    }

    private boolean isOptionalAssertMap(Method method) {
      return method.getDeclaringClass().equals(AbstractOptionalAssert.class) && method.getName().contains("map");
    }

    private boolean isOptionalAssertFlatMap(Method method) {
      return method.getDeclaringClass().equals(AbstractOptionalAssert.class) && method.getName().contains("flatMap");
    }
  }
}
