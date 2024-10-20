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

import static org.assertj.core.api.AllowMethod.forCollection;
import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

/** Base class for tests finding mutating methods in classes which implement {@link Collection}. */
abstract class MutatingMethodFinder_Collection_Test {
  /** The instance to test. */
  protected final CollectionVisitor<Optional<String>> finder = new MutatingMethodFinder();

  /**
   * Makes all methods other than creating an iterator throw {@link UnsupportedOperationException}.
   * This is useful for testing a collection where none of the collection mutating methods
   * are supported but the collection has a mutating iterator.
   */
  private static final class IteratorHandler implements InvocationHandler {
    private final Collection<?> target;

    /** Creates a new iterator. */
    private final String iteratorMethod;

    /** The iterator to return from the iterator method. */
    private final Iterator<?> iterator;

    /**
     * Creates a new {@link IteratorHandler}.
     *
     * @param target collection to handle actual calls
     * @param iteratorMethod creates an iterator
     * @param iterator the iterator to return
     */
    private IteratorHandler(final Collection<?> target, final String iteratorMethod, final Iterator<?> iterator) {
      this.target = target;
      this.iteratorMethod = iteratorMethod;
      this.iterator = iterator;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      if (method.getName().equals(iteratorMethod)) return iterator;
      if (method.getName().equals("isEmpty") || method.getName().equals("iterator")) {
        return method.invoke(target, args);
      }
      throw new UnsupportedOperationException(method.getName());
    }
  }

  /** Mutating methods in {@link Collection}. */
  protected static Stream<Arguments> collectionMethods() {
    return Stream.of(Arguments.of("add", 1),
                     Arguments.of("addAll", 1),
                     Arguments.of("clear", 0),
                     Arguments.of("remove", 1),
                     Arguments.of("removeAll", 1),
                     Arguments.of("removeIf", 1),
                     Arguments.of("retainAll", 1));
  }

  /**
   * Tests a collection with a single mutating method.
   *
   * @param interfaceType the collection interface to test
   * @param target a mutating instance to delegate the single mutating method to
   * @param method the mutating method to try to detect
   * @param argumentCount the number of arguments this method takes, us to disambiguate multiple
   *     methods with the same name
   */
  protected final void testOneMutatingMethodInCollection(final Class<?> interfaceType,
                                                         final Collection<String> target,
                                                         final String method,
                                                         final int argumentCount) {
    // GIVEN
    Collection<String> proxyInstance = forCollection(interfaceType, target, method, argumentCount);
    // WHEN
    Optional<String> actual = finder.visitCollection(proxyInstance);
    // THEN
    assertThat(actual).hasValueSatisfying(msg -> assertThat(msg).contains(method));
  }

  /**
   * Tests a mutating iterator.
   *
   * @param interfaceType the type of collection to create
   * @param iteratorMethod method which creates the iterator
   * @param iterator mock iterator, configured appropriately for the test
   * @param method successful mutating method in the iterator to expect
   */
  protected final void testIterator(final Class<?> interfaceType,
                                    final Collection<?> target,
                                    final String iteratorMethod,
                                    final Iterator<?> iterator,
                                    final String method) {
    // GIVEN
    Collection<?> proxyInstance = forIterator(interfaceType, target, iteratorMethod, iterator);
    // WHEN
    Optional<String> actual = finder.visitCollection(proxyInstance);
    // THEN
    assertThat(actual).hasValueSatisfying(msg -> assertThat(msg).containsIgnoringCase(String.format("iterator().%s", method)));
  }

  /**
   * Creates an implementation of an interface in which everything other than creating an iterator
   * {@link UnsupportedOperationException}.
   *
   * @param interfaceType the collection interface to test
   * @param iteratorMethod the iterator method to test
   * @param iterator the iterator to return from methods which create an iterator
   */
  private final <T> T forIterator(final Class<?> interfaceType, final Collection<?> target, final String iteratorMethod,
                                  final Iterator<?> iterator) {
    return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                                      new Class[] { interfaceType },
                                      new IteratorHandler(target, iteratorMethod, iterator));
  }
}
