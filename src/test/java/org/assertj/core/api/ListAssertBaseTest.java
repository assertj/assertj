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
package org.assertj.core.api;

import static org.mockito.Mockito.mock;

import java.util.Collections;
import java.util.List;

import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.Lists;


/**
 * Base class for {@link ListAssert} tests.
 * 
 * @author Olivier Michallat
 */
public abstract class ListAssertBaseTest extends BaseTestTemplate<ListAssert<String>, List<? extends String>> {

  protected Lists lists;

  @Override
  protected ListAssert<String> create_assertions() {
    return new ListAssert<>(Collections.<String> emptyList());
  }

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    lists = mock(Lists.class);
    assertions.lists = lists;
  }

  protected Lists getLists(ListAssert<String> assertions) {
    return assertions.lists;
  }

  protected Iterables getIterables(ListAssert<String> assertions) {
    return assertions.iterables;
  }
  
  
}
