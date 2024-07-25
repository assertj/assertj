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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import static java.util.stream.Collectors.toList;

import java.util.Iterator;

import org.assertj.core.util.Streams;

/**
 * Assertion methods for {@link Iterable}.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(Iterable)}</code>.
 * </p>
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Matthieu Baechler
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 * @author Julien Meddah
 *
 * @param <ELEMENT> the type of elements of the "actual" value.
 */
public class IterableAssert<ELEMENT> extends
    FactoryBasedNavigableIterableAssert<IterableAssert<ELEMENT>, Iterable<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> {

  public IterableAssert(Iterable<? extends ELEMENT> actual) {
    super(actual, IterableAssert.class, ObjectAssert::new);
  }

  @Override
  protected IterableAssert<ELEMENT> newAbstractIterableAssert(Iterable<? extends ELEMENT> iterable) {
    return assertThatIterable(iterable);
  }

  public static <ELEMENT> IterableAssert<ELEMENT> assertThatIterable(Iterable<? extends ELEMENT> actual) {
    return new IterableAssert<>(actual);
  }

  static <T> Iterable<T> toIterable(Iterator<T> iterator) {
    return Streams.stream(iterator).collect(toList());
  }

}
