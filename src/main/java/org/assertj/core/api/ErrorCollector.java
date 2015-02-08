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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.api;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Collects error messages of all AssertionErrors thrown by the proxied method. */
class ErrorCollector implements MethodInterceptor {

  private final List<Throwable> errors = new ArrayList<Throwable>();

  @Override
  public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
    try {
      proxy.invokeSuper(obj, args);
    } catch (AssertionError e) {
      errors.add(e);
    }
    return obj;
  }

  public List<Throwable> errors() {
    return Collections.unmodifiableList(errors);
  }
}
