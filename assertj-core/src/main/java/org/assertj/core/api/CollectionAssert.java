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
 * <p>
 *
 * @param <ELEMENT> the type of elements of the "actual" value.
 *
 * @since 3.21.0
 */
public class CollectionAssert<SELF extends CollectionAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT>,
                              ACTUAL extends Collection<? extends ELEMENT>,
                              ELEMENT,
                              ELEMENT_ASSERT extends AbstractAssert<? extends ELEMENT_ASSERT, ELEMENT>>
  extends AbstractCollectionAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT> {

  private final AssertFactory<ELEMENT, ELEMENT_ASSERT> assertFactory;

  public static <ELEMENT> AbstractCollectionAssert<?, Collection<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> assertThatCollection(Collection<? extends ELEMENT> actual) {
    return new CollectionAssert<>(actual);
  }

  @SuppressWarnings("unchecked")
  public CollectionAssert(ACTUAL actual) {
    super(actual, CollectionAssert.class);
    this.assertFactory = value -> (ELEMENT_ASSERT) new ObjectAssert<>(value);
  }

  CollectionAssert(ACTUAL actual, AssertFactory<ELEMENT, ELEMENT_ASSERT> assertFactory) {
    super(actual, CollectionAssert.class);
    this.assertFactory = assertFactory;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected ELEMENT_ASSERT toAssert(ELEMENT value, String description) {
    return assertFactory.createAssert(value).as(description);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  protected SELF newAbstractIterableAssert(Iterable<? extends ELEMENT> iterable) {
    return (SELF) new CollectionAssert(newArrayList(iterable));
  }

}
