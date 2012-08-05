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
package org.fest.assertions.api.list;

import static org.mockito.Mockito.verify;

import org.fest.assertions.api.AbstractIterableAssert;
import org.fest.assertions.api.ListAssert;
import org.fest.assertions.api.ListAssertBaseTest;

/**
 * Tests for <code>{@link AbstractIterableAssert#isSorted()}</code>.
 * 
 * @author Joel Costigliola
 */
public class ListAssert_isSorted_Test extends ListAssertBaseTest {

  @Override
  protected ListAssert<String> invoke_api_method() {
    return assertions.isSorted();
  }

  @Override
  protected void verify_internal_effects() {
    verify(lists).assertIsSorted(getInfo(assertions), getActual(assertions));
  }
}
