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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.util.Arrays.array;

import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Concrete assertions for arrays of objects without any final methods to allow proxying.
 */
public class ProxyableObjectArrayAssert<ELEMENT> extends AbstractObjectArrayAssert<ProxyableObjectArrayAssert<ELEMENT>, ELEMENT> {

  public ProxyableObjectArrayAssert(ELEMENT[] actual) {
    super(actual, ProxyableObjectArrayAssert.class);
  }

  public ProxyableObjectArrayAssert(AtomicReferenceArray<ELEMENT> actual) {
    this(array(actual));
  }

  public ProxyableObjectArrayAssert(ProxyableObjectArrayAssert<ELEMENT> actual) {
    this(actual.actual);
  }

  @Override
  protected ProxyableObjectArrayAssert<ELEMENT> newObjectArrayAssert(ELEMENT[] array) {
    return new ProxyableObjectArrayAssert<>(array);
  }

  @Override
  protected <E> AbstractListAssert<?, List<? extends E>, E, ObjectAssert<E>> newListAssertInstance(List<? extends E> newActual) {
    return new ProxyableListAssert<>(newActual);
  }

}
