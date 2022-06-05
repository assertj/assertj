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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.util;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


/**
 * Operations to perform on streams.
 *
 * @author Stefano Cordio
 * @author Ashley Scopes
 */
public final class Streams {

  private Streams() {
  }

  /**
   * Returns a sequential {@link Stream} of the contents of {@code iterable}, delegating to {@link
   * Collection#stream} if possible.
   * @param <T> the stream type
   * @param iterable the iterable to build the stream from
   * @return the stream built from the given {@link Iterable}
   */
  public static <T> Stream<T> stream(Iterable<T> iterable) {
    return (iterable instanceof Collection)
        ? ((Collection<T>) iterable).stream()
        : StreamSupport.stream(iterable.spliterator(), false);
  }

  /**
   * Returns a sequential {@link Stream} of the contents of {@code iterator}.
   *
   * <p>The process of using the returned stream will exhaust the iterator lazily.
   *
   * @param <T> the stream type
   * @param iterator the iterator to build the stream from
   * @return the stream built from the given {@link Iterator}
   */
  public static <T> Stream<T> stream(Iterator<T> iterator) {
    return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
  }

  /**
   * Returns a sequential {@link Stream} of the contents of {@code enumeration}.
   *
   * <p>The process of using the returned stream will exhaust the enumeration lazily.
   *
   * @param <T> the stream type
   * @param enumeration the enumeration to build the stream from.
   * @return the stream built from the given {@link Enumeration}.
   */
  public static <T> Stream<T> stream(Enumeration<T> enumeration) {
    // TODO(ascopes): replace with Enumeration#asIterator when we drop Java 8 support.
    return stream(new Iterator<T>() {
      @Override
      public boolean hasNext() {
        return enumeration.hasMoreElements();
      }

      @Override
      public T next() {
        return enumeration.nextElement();
      }
    });
  }
}
