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

import java.util.Collection;

public class ConcreteIterableAssert<ELEMENT> extends
    FactoryBasedNavigableIterableAssert<ConcreteIterableAssert<ELEMENT>, Iterable<ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> {

  public ConcreteIterableAssert(Collection<ELEMENT> actual) {
    super(actual, ConcreteIterableAssert.class, new ObjectAssertFactory<ELEMENT>());
  }

  @Override
  public ObjectAssert<ELEMENT> toAssert(ELEMENT value, String description) {
    return new ObjectAssert<>(value);
  }
}
