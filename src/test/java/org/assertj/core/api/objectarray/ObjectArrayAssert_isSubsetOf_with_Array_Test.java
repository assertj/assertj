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
package org.assertj.core.api.objectarray;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Arrays.array;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.ObjectArrayAssert;
import org.assertj.core.api.ObjectArrayAssertBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link ObjectArrayAssert#isSubsetOf(Object[])}</code>.
 */
public class ObjectArrayAssert_isSubsetOf_with_Array_Test extends ObjectArrayAssertBaseTest {

  private final Object [] values = array("Yoda", "Luke");

  @Override
  protected ObjectArrayAssert<Object> invoke_api_method() {
    return assertions.isSubsetOf(values);
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertIsSubsetOf(getInfo(assertions), getActual(assertions), asList(values));
  }
  
  @Test
  public void invoke_api_like_user() {
    assertThat(array("Luke", "Yoda")).isSubsetOf("Yoda", "Luke", "Chewbacca");
  }
}
