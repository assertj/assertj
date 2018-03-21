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
package org.assertj.core.api.list;

import org.assertj.core.api.ListAssert;
import org.assertj.core.api.ListAssertBaseTest;
import org.assertj.core.data.Index;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.TestData.someIndex;
import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link ListAssert#satisfies(Consumer, Index)}</code>
 *
 * @author Jacek Jackowiak
 */
public class ListAssert_satisfies_Test extends ListAssertBaseTest {

  private final Index index = someIndex();
  private final Consumer<String> consumer = str -> assertThat(str).isNotEmpty();

  @Override
  protected ListAssert<String> invoke_api_method() {
    return assertions.satisfies(consumer, index);
  }

  @Override
  protected void verify_internal_effects() {
    verify(lists).satisfies(info(), getActual(assertions), consumer, index);
  }
}
