/*
 * Created on Sep 30, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.api;

import static java.util.Collections.emptyList;
import static org.mockito.Mockito.mock;

import org.fest.assertions.internal.Iterables;
import org.junit.Before;

/**
 * Tests for <code>{@link AbstractIterableAssert#contains(Object...)}</code>.
 *
 * @author Joel Costigliola
 */
public abstract class AbstractTest_for_IterableAssert {

  protected Iterables iterables;
  protected ConcreteIterableAssert<Object> assertions;

  @Before public void setUp() {
    iterables = mock(Iterables.class);
    assertions = new ConcreteIterableAssert<Object>(emptyList());
    assertions.iterables = iterables;
  }

}
