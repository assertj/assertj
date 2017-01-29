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
package org.assertj.core.api.list;

import static org.assertj.core.test.TestData.someIndex;

import org.assertj.core.api.ListAssert;
import org.assertj.core.api.ListAssertBaseTest;
import org.assertj.core.data.Index;

import static org.mockito.Mockito.verify;


/**
 * Tests for <code>{@link ListAssert#contains(Object, Index)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class ListAssert_contains_at_Index_Test extends ListAssertBaseTest {

  private final Index index = someIndex();

  @Override
  protected ListAssert<String> invoke_api_method() {
    return assertions.contains("Yoda", index);
  }

  @Override
  protected void verify_internal_effects() {
    verify(lists).assertContains(getInfo(assertions), getActual(assertions), "Yoda", index);
  }
}
