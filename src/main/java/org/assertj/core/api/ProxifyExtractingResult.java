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

import static org.assertj.core.util.Preconditions.checkState;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Optional;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

class ProxifyExtractingResult implements MethodInterceptor {

  private final SoftProxies proxies;

  ProxifyExtractingResult(SoftProxies proxies) {
    this.proxies = proxies;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {

    Object result = proxy.invokeSuper(obj, args);
    return proxies.create(result.getClass(), actualClass(result), actual(result));
  }

  @SuppressWarnings("rawtypes")
  private static Class actualClass(Object result) {
    if (result instanceof ObjectArrayAssert) {
      return Array.newInstance(Object.class, 0).getClass();
    }
    if (result instanceof OptionalAssert) {
      return Optional.class;
    }

    // Trying to create a proxy with cglib will only match exact constructor argument types.
    // To initialize one for ListAssert for example we can't use an ArrayList, we have to use a List.
    // So we can't just return actual.getClass() as we could read a concrete class whereas
    // *Assert classes define a constructor using interface (@see ListAssert for example).
    //
    // Instead we can read generic types from *Assert definition.
    // Inspecting: class ListAssert<T> extends AbstractListAssert<ListAssert<T>, List<? extends T>, T>
    // will return the generic defined by the super class AbstractListAssert at index 1, which is a List<? extends T>
    Type actualType = ((ParameterizedType) result.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    if (actualType instanceof ParameterizedType) {
      return (Class<?>) ((ParameterizedType) actualType).getRawType();
    }
    if (actualType instanceof TypeVariable) {
      return (Class<?>) ((TypeVariable) actualType).getGenericDeclaration();
    }

    return (Class<?>) actualType;
  }

  private static Object actual(Object result) {
    checkState(result instanceof AbstractAssert, "We should be trying to make a proxy of an *Assert class.");
    return ((AbstractAssert<?, ?>) result).actual;
  }

}
