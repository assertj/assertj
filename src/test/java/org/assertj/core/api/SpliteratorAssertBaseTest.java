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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api;

import static org.mockito.Mockito.mock;

import java.util.Spliterator;

import org.assertj.core.internal.Spliterators;
import org.assertj.core.test.StringSpliterator;

public abstract class SpliteratorAssertBaseTest extends BaseTestTemplate<SpliteratorAssert<String>, Spliterator<String>> {

  protected Spliterators spliterators;

  @Override
  protected SpliteratorAssert<String> create_assertions() {
    return new SpliteratorAssert<>(new StringSpliterator());
  }

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    spliterators = mock(Spliterators.class);
    assertions.spliterators = spliterators;
  }

  protected Spliterators getSpliterators(SpliteratorAssert<?> assertions) {
    return assertions.spliterators;
  }
}
