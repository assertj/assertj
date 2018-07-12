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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api;

import org.assertj.core.internal.Iterators;
import org.assertj.core.util.VisibleForTesting;

import java.util.Iterator;

public abstract class AbstractIteratorAssert<SELF extends AbstractIteratorAssert<SELF, ELEMENT>, ELEMENT>
    extends AbstractAssert<SELF, Iterator<? extends ELEMENT>> {

  @VisibleForTesting
  Iterators iterators = Iterators.instance();

  public AbstractIteratorAssert(Iterator<? extends ELEMENT> actual, Class<?> selfType) {
    super(actual, selfType);
  }

  public SELF hasNext() {
    iterators.assertHasNext(info, actual);
    return myself;
  }

  public SELF isExhausted() {
    iterators.assertIsExhausted(info, actual);
    return myself;
  }

  public IterableAssert<ELEMENT> toIterable() {
    return new IterableAssert<>(IterableAssert.toIterable(actual));
  }

}
