/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api;

import java.util.Iterator;
import java.util.List;

/**
 * Concrete assertions for {@link Iterable}s without any final methods to allow proxying.
 * 
 * @author Gaël LHEZ
 * @since 2.5.1 / 3.5.1
 */
public class SoftAssertionIterableAssert<ELEMENT> extends
    FactoryBasedNavigableIterableAssert<SoftAssertionIterableAssert<ELEMENT>, Iterable<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> {

  public SoftAssertionIterableAssert(Iterable<? extends ELEMENT> actual) {
    super(actual, SoftAssertionIterableAssert.class, new ObjectAssertFactory<ELEMENT>());
  }

  public SoftAssertionIterableAssert(Iterator<? extends ELEMENT> actual) {
    this(toLazyIterable(actual));
  }

  @Override
  protected <ELEMENT2> AbstractListAssert<?, List<? extends ELEMENT2>, ELEMENT2, ObjectAssert<ELEMENT2>> newListAssertInstance(List<? extends ELEMENT2> newActual) {
    return new SoftAssertionListAssert<>(newActual);
  }

}
