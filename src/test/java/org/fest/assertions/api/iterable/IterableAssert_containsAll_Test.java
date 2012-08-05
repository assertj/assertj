/*
 * Created on Sep 30, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.api.iterable;

import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.fest.assertions.api.AbstractIterableAssert;
import org.fest.assertions.api.ConcreteIterableAssert;
import org.fest.assertions.api.IterableAssertBaseTest;

/**
 * Tests for <code>{@link AbstractIterableAssert#containsAll(Iterable)}</code>.
 * 
 * @author Joel Costigliola
 */
public class IterableAssert_containsAll_Test extends IterableAssertBaseTest {

  private List<Object> list = new ArrayList<Object>();

  @Override
  protected ConcreteIterableAssert<Object> invoke_api_method() {
    return assertions.containsAll(list);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertContainsAll(getInfo(assertions), getActual(assertions), list);
  }
}
