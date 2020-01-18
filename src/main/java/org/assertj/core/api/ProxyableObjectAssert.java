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

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Concrete assertions for {@link Object}s without any final methods to allow proxying.
 */
public class ProxyableObjectAssert<ACTUAL> extends AbstractObjectAssert<ProxyableObjectAssert<ACTUAL>, ACTUAL> {

  public ProxyableObjectAssert(ACTUAL actual) {
    super(actual, ProxyableObjectAssert.class);
  }

  public ProxyableObjectAssert(AtomicReference<ACTUAL> actual) {
    this(actual == null ? null: actual.get());
  }

  @Override
  protected <ELEMENT> AbstractListAssert<?, List<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> newListAssertInstance(List<? extends ELEMENT> newActual) {
    return new ProxyableListAssert<>(newActual);
  }

  @Override
  protected <T> AbstractObjectAssert<?, T> newObjectAssert(T objectUnderTest) {
    return new ProxyableObjectAssert<>(objectUnderTest);
  }
}
