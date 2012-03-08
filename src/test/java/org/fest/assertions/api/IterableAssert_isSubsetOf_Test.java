/*
 * Created on Feb 18, 2012
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
 * Copyright @2012 the original author or authors.
 */
package org.fest.assertions.api;

import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.verify;

import java.util.List;

import static org.fest.util.Collections.*;
import org.junit.Test;

/**
 * Tests for <code>{@link AbstractIterableAssert#isSubsetOf(Iterable)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Maciej Jaskowski
 */
public class IterableAssert_isSubsetOf_Test extends AbstractTest_for_IterableAssert {

  @Test
  public void should_verify_that_actual_contains_given_values() {
    List<String> values = list("Yoda", "Luke");
    assertions.isSubsetOf(values);
    verify(iterables).assertIsSubsetOf(assertions.info, assertions.actual, values);
  }

  @Test
  public void should_return_this() {
    ConcreteIterableAssert returned = assertions.isSubsetOf(list("Luke"));
    assertSame(assertions, returned);
  }
}
