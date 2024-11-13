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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

import static org.assertj.core.api.AllowMethod.forIterator;

/**
 * Creates an immutable iterator and delegates all other calls to another invocation handler.
 */
final class AllowCollectionMethod implements InvocationHandler {
  /** Handles the call to the supported method. */
  private final Object target;

  /** Handles everything other than {@code iterator()}. */
  private final InvocationHandler handler;

  /**
   * Creates a new {@link AllowCollectionMethod}.
   *
   * @param target handles calls to the allowed method
   * @param handler handles everything other than {@code iterator()}
   */
  AllowCollectionMethod(Object target, InvocationHandler handler) {
    Objects.requireNonNull(target, "target");
    Objects.requireNonNull(handler, "handler");
    this.target = target;
    this.handler = handler;
  }

  @Override
  public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
    Objects.requireNonNull(proxy, "proxy");
    Objects.requireNonNull(method, "method");

    return (method.getName().equals("iterator"))
        ? forIterator(method.invoke(target, args))
        : handler.invoke(proxy, method, args);
  }
}
