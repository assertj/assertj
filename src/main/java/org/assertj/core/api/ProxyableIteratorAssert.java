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

import java.util.Iterator;
import java.util.List;

/**
 * Concrete assertions for {@link Iterator}s without any final methods to allow proxying.
 * 
 * @author Stephan Windmüller
 * @since 3.11.0
 */
public class ProxyableIteratorAssert<ELEMENT>
    extends AbstractIteratorAssert<ProxyableIteratorAssert<ELEMENT>, ELEMENT> {

  public ProxyableIteratorAssert(Iterator<? extends ELEMENT> actual) {
    super(actual, ProxyableIteratorAssert.class);
  }

  @Override
  protected <ELEMENT2> AbstractListAssert<?, List<? extends ELEMENT2>, ELEMENT2, ObjectAssert<ELEMENT2>> newListAssertInstance(List<? extends ELEMENT2> newActual) {
    return new ProxyableListAssert<>(newActual);
  }

}
