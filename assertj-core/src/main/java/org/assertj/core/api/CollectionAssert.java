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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.util.Lists.newArrayList;

import java.util.Collection;

/**
 * Assertion methods for {@link Collection}s.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(Collection)}</code>.
 * <p>
 *
 * @param <ELEMENT> the type of elements of the "actual" value.
 *
 * @since 3.21.0
 */
public class CollectionAssert<ELEMENT> extends
    AbstractCollectionAssert<CollectionAssert<ELEMENT>, Collection<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> {

  public static <E> AbstractCollectionAssert<?, Collection<? extends E>, E, ObjectAssert<E>> assertThatCollection(Collection<? extends E> actual) {
    return new CollectionAssert<>(actual);
  }

  public CollectionAssert(Collection<? extends ELEMENT> actual) {
    super(actual, CollectionAssert.class);
  }

  @Override
  protected ObjectAssert<ELEMENT> toAssert(ELEMENT value, String description) {
    return new ObjectAssert<>(value).as(description);
  }

  @Override
  protected CollectionAssert<ELEMENT> newAbstractIterableAssert(Iterable<? extends ELEMENT> iterable) {
    return new CollectionAssert<>(newArrayList(iterable));
  }

}
