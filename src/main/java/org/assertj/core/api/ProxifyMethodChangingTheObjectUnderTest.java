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
package org.assertj.core.api;

import static org.assertj.core.util.Preconditions.checkState;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;

import net.bytebuddy.implementation.bind.annotation.FieldValue;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;

public class ProxifyMethodChangingTheObjectUnderTest {

  public static final String FIELD_NAME = "dispatcher";

  private final SoftProxies proxies;

  ProxifyMethodChangingTheObjectUnderTest(SoftProxies proxies) {
    this.proxies = proxies;
  }

  @RuntimeType
  public static AbstractAssert<?, ?> intercept(
    @FieldValue(FIELD_NAME) ProxifyMethodChangingTheObjectUnderTest dispatcher,
    @SuperCall Callable<AbstractAssert<?, ?>> assertionMethod,
    @This AbstractAssert<?, ?> currentAssertInstance) throws Exception {
    Object result = assertionMethod.call();
    return dispatcher.createAssertProxy(result).withAssertionState(currentAssertInstance);
  }

  // can't return AbstractAssert<?, ?> otherwise withAssertionState(currentAssertInstance) does not compile.
  @SuppressWarnings({ "unchecked", "rawtypes" })
  protected AbstractAssert createAssertProxy(Object currentActual) {
    if (currentActual instanceof IterableSizeAssert) {
      IterableSizeAssert<?> iterableSizeAssert = (IterableSizeAssert<?>) currentActual;
      // can' use the usual way of building soft proxy since IterableSizeAssert takes 2 parameters
      return proxies.createIterableSizeAssertProxy(iterableSizeAssert);
    }
    if (currentActual instanceof MapSizeAssert) {
      MapSizeAssert<?, ?> iterableSizeAssert = (MapSizeAssert<?, ?>) currentActual;
      // can' use the usual way of building soft proxy since IterableSizeAssert takes 2 parameters
      return proxies.createMapSizeAssertProxy(iterableSizeAssert);
    }
    return (AbstractAssert) proxies.create(currentActual.getClass(), actualClass(currentActual), actual(currentActual));
  }

  @SuppressWarnings("rawtypes")
  private static Class actualClass(Object result) {
    if (result instanceof ObjectArrayAssert || result instanceof ProxyableObjectArrayAssert) {
      return Array.newInstance(Object.class, 0).getClass();
    }
    if (result instanceof OptionalAssert) {
      return Optional.class;
    }
    if (result instanceof ObjectAssert) {
      return Object.class;
    }
    if (result instanceof MapAssert) {
      return Map.class;
    }

    // Trying to create a proxy will only match exact constructor argument types.
    // To initialize one for ListAssert for example we can't use an ArrayList, we have to use a List.
    // So we can't just return actual.getClass() as we could read a concrete class whereas
    // *Assert classes define a constructor using interface (@see ListAssert for example).
    //
    // Instead we can read generic types from *Assert definition.
    // Inspecting: class ListAssert<T> extends AbstractListAssert<ListAssert<T>, List<? extends T>, T>
    // will return the generic defined by the super class AbstractListAssert at index 1, which is a List<? extends T>
    Type actualType = ((ParameterizedType) result.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    if (actualType instanceof ParameterizedType) return (Class<?>) ((ParameterizedType) actualType).getRawType();
    if (actualType instanceof TypeVariable) return (Class<?>) ((TypeVariable) actualType).getGenericDeclaration();
    return (Class<?>) actualType;
  }

  private static Object actual(Object result) {
    checkState(result instanceof AbstractAssert, "We should be trying to make a proxy of an *Assert class.");
    return ((AbstractAssert<?, ?>) result).actual;
  }

}
