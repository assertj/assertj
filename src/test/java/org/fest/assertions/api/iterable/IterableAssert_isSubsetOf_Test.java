/*
 * Created on Feb 18, 2012
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
 * Copyright @2012 the original author or authors.
 */
package org.fest.assertions.api.iterable;

import static org.fest.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.fest.assertions.api.AbstractIterableAssert;
import org.fest.assertions.api.ConcreteIterableAssert;
import org.fest.assertions.api.IterableAssertBaseTest;

/**
 * Tests for <code>{@link AbstractIterableAssert#isSubsetOf(Iterable)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Maciej Jaskowski
 */
public class IterableAssert_isSubsetOf_Test extends IterableAssertBaseTest {

  private final List<String> values = newArrayList("Yoda", "Luke");

  @Override
  protected ConcreteIterableAssert<Object> invoke_api_method() {
    return assertions.isSubsetOf(values);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertIsSubsetOf(getInfo(assertions), getActual(assertions), values);
  }
}
