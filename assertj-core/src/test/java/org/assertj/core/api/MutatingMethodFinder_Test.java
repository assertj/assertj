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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Optional;

/** Base class for tests for a specific collection type for {@link MutatingMethodFinder}. */
abstract class MutatingMethodFinder_Test {
  /**
   * Makes every the methods but one throw an {@link UnsupportedOperationException}. This is useful
   * for verify that every mutating method is detected. All the standard mutable collections
   * implement {@code add()}, so if you just test with one of these classes, you don't test for
   * some more obscure implementation that implements add but doesn't implement one of the other
   * mutating methods.
   */
  private static final class OneSupportedMethod implements InvocationHandler {
    /** Handles the call to the supported method. */
    private final Object target;

    /** The name of the only method that won't throw {@link UnsupportedOperationException}. */
    private final String supportedMethod;

    /**
     * The number of arguments the supported method expects, in case there is more than one method
     * with the same name.
     */
    private final int argumentCount;

    /**
     * Creates a new {@link OneSupportedMethod}.
     *
     * @param target a mutable collection or map
     * @param supportedMethod the name of the only supported method
     * @param argumentCount the number of arguments this method accepts
     */
    private OneSupportedMethod(Object target, String supportedMethod, int argumentCount) {
      this.target = target;
      this.supportedMethod = supportedMethod;
      this.argumentCount = argumentCount;
    }

    /**
     * Counts the actual arguments, handling a {@code null} argument array which indicates a
     * function that doesn't have arguments.
     */
    private int countArguments(Object[] args) {
      return args == null ? 0 : args.length;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      if (!method.getName().equals(supportedMethod) || argumentCount != countArguments(args)) {
        throw new UnsupportedOperationException(method.getName());
      }

      try {
        return method.invoke(target, args);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    }
  }

  /** The instance to test. */
  protected final CollectionVisitor<Optional<String>> finder = new MutatingMethodFinder();

  /**
   * Creates an implementation of an interface in which all methods but one throw {@link
   * UnsupportedOperationException}.
   *
   * @param interfaceType the collection interface to test
   * @param target a mutating instance to delegate the single mutating method to
   * @param method the mutating method to try to detect
   * @param argumentCount the number of arguments this method takes, us to disambiguate multiple
   *     methods with the same name
   */
  protected final <T> T withMutatingMethod(
                                           final Class<?> interfaceType, final T target, final String method,
                                           final int argumentCount) {
    return (T) Proxy.newProxyInstance(
                                      this.getClass().getClassLoader(),
                                      new Class[] { interfaceType },
                                      new OneSupportedMethod(target, method, argumentCount));
  }
}
