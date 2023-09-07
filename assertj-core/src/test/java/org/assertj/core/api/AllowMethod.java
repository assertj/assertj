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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Iterator;
import java.util.Objects;

/**
 * Makes most methods throw an {@link UnsupportedOperationException}. This is useful for
 * to verify that every mutating method is detected.
 */
final class AllowMethod implements InvocationHandler {
  /** Argument count to method name. */
  private final Multimap<Integer, String> allowed;

  /** Handles calls to allowed methods. */
  private final Object target;

  /**
   * Creates a new {@link AllowMethod}.
   *
   * @param target handles calls to the allowed method
   * @param allowed argument count to method name
   */
  AllowMethod(Object target, Multimap<Integer, String> allowed) {
    Objects.requireNonNull(target, "target");
    Objects.requireNonNull(allowed, "allowed");
    this.target = target;
    this.allowed = ImmutableMultimap.copyOf(allowed);
  }

  /**
   * Counts the actual arguments, handling a {@code null} argument array used for a no-argument
   * method.
   */
  private int countArguments(Object[] args) {
    return args == null ? 0 : args.length;
  }

  @Override
  public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
    Objects.requireNonNull(proxy, "proxy");
    Objects.requireNonNull(method, "method");

    if (allowed.get(countArguments(args)).contains(method.getName())) {
      try {
        return method.invoke(target, args);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    }

    throw new UnsupportedOperationException(method.getName());
  }

  /**
   * An iterator which only supports {@code next()}.
   *
   * @param target iterator to delegate {@code next()} calls to.
   */
  static <E> Iterator<E> forIterator(final Object target) {
    return (Iterator<E>) Proxy.newProxyInstance(
                                                target.getClass().getClassLoader(),
                                                new Class[] { Iterator.class },
                                                new AllowMethod(target, ImmutableMultimap.of(0, "next")));
  }

  /**
   * Collection which supports a few selected non-mutating methods and one mutating method.
   *
   * @param interfaceType the collection interface to test
   * @param target a mutating instance to delegate the single mutating method to
   * @param method the mutating method to try to detect
   * @param argumentCount the number of arguments this method takes
   */
  static <T> T forCollection(
                             final Class<?> interfaceType, final T target, final String method, final int argumentCount) {
    Multimap<Integer, String> allowed = ImmutableMultimap.<Integer, String> builder()
                                                         .putAll(0, ImmutableSet.of("isEmpty", "size"))
                                                         .put(argumentCount, method)
                                                         .build();
    return (T) Proxy.newProxyInstance(
                                      target.getClass().getClassLoader(),
                                      new Class[] { interfaceType },
                                      new AllowCollectionMethod(target, new AllowMethod(target, allowed)));
  }

  /**
   * Map which supports a few selected non-mutating methods and one mutating method.
   *
   * @param interfaceType the collection interface to test
   * @param target a mutating instance to delegate the single mutating method to
   * @param method the mutating method to try to detect
   * @param argumentCount the number of arguments this method takes
   */
  static <T> T forMap(
                      final Class<?> interfaceType, final T target, final String method, final int argumentCount) {
    Multimap<Integer, String> allowed = ImmutableMultimap.<Integer, String> builder()
                                                         .putAll(0, ImmutableSet.of("isEmpty", "keySet"))
                                                         .put(argumentCount, method)
                                                         .put(1, "get")
                                                         .build();

    return (T) Proxy.newProxyInstance(
                                      target.getClass().getClassLoader(),
                                      new Class[] { interfaceType },
                                      new AllowMethod(target, allowed));
  }
}
