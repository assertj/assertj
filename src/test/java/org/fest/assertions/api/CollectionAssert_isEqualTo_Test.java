/*
 * Created on Aug 3, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.api;

import static java.util.Collections.emptyList;
import static junit.framework.Assert.assertEquals;

import java.util.*;

import org.junit.*;

/**
 * Test for <code>{@link CollectionAssert#isEqualTo(Collection)}</code>.
 *
 * TODO finish
 *
 * @author Yvonne Wang
 */
public class CollectionAssert_isEqualTo_Test {

  private CollectionAssert assertions;

  @Before
  public void setUp() {
    assertions = new CollectionAssert(emptyList());
  }

  @Test
  public void should_return_true_if_expected_value_is_equal_to_actual_value() {
    Collection<String> c = Arrays.asList("one","two");
    CollectionAssert a = assertions.isEqualTo(c);
    assertEquals(c, a);
  }
}
