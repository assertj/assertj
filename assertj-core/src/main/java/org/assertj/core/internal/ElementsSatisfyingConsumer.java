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
package org.assertj.core.internal;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.internal.Iterables.byPassingAssertions;
import static org.assertj.core.util.Streams.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Wrapper for the list of elements that satisfy certain requirements (expressed as a <code>Consumer</code>).
 *
 * @author Michael Grafl
 *
 * @param <E> element type
 */
class ElementsSatisfyingConsumer<E> {
  private final List<E> elements;

  ElementsSatisfyingConsumer(Iterable<? extends E> actual, Consumer<? super E> assertions) {
    this(filterByPassingAssertions(actual, assertions));
  }

  private ElementsSatisfyingConsumer(List<E> elements) {
    this.elements = elements;
  }

  List<E> getElements() {
    return elements;
  }

  /**
   * New <code>ElementsSatisfyingConsumer</code> containing all elements except the (first occurrence of the) given element.
   *
   * <p> This instance is not modified.
   *
   * @param element the element to remove from the result
   * @return all except the given element
   */
  ElementsSatisfyingConsumer<E> withoutElement(E element) {
    ArrayList<E> listWithoutElement = new ArrayList<>(elements);
    listWithoutElement.remove(element);
    return new ElementsSatisfyingConsumer<>(listWithoutElement);
  }

  private static <E> List<E> filterByPassingAssertions(Iterable<? extends E> actual, Consumer<? super E> assertions) {
    return stream(actual).filter(byPassingAssertions(assertions)).collect(toList());
  }
}
