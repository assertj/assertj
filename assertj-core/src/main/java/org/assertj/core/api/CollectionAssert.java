/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api;

import static org.assertj.core.util.Lists.newArrayList;

import java.util.Collection;

/**
 * Assertion methods for {@link Collection}s.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(Collection)}</code>.
 *
 * @param <ELEMENT> the type of elements of the "actual" value.
 *
 * @since 3.21.0
 */
public class CollectionAssert<ELEMENT> extends
    AbstractCollectionAssert<CollectionAssert<ELEMENT>, Collection<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> {

  /**
   * Creates a collection assertion.
   *
   * @param <E> the collection element type
   * @param actual the actual collection
   * @return the created assertion object
   */
  public static <E> AbstractCollectionAssert<?, Collection<? extends E>, E, ObjectAssert<E>> assertThatCollection(Collection<? extends E> actual) {
    return new CollectionAssert<>(actual);
  }

  /**
   * Creates a new collection assertion.
   *
   * @param actual the actual collection to verify
   */
  public CollectionAssert(Collection<? extends ELEMENT> actual) {
    super(actual, CollectionAssert.class);
  }

  @Override
  protected ObjectAssert<ELEMENT> toAssert(ELEMENT value) {
    return new ObjectAssert<>(value);
  }

  @Override
  protected CollectionAssert<ELEMENT> newAbstractIterableAssert(Iterable<? extends ELEMENT> iterable) {
    return new CollectionAssert<>(newArrayList(iterable));
  }

  /**
   * Creates a collection assertion used for null navigation.
   *
   * @param <ELEMENT> the collection element type
   * @return the null-navigation assertion
   */
  public static <ELEMENT> CollectionAssert<ELEMENT> nullCollectionAssert() {
    return new CollectionAssert<>(null);
  }

}
