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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api;

import static org.mockito.Mockito.mock;

import org.assertj.core.internal.Comparables;
import org.assertj.core.internal.Strings;

public abstract class StringAssertBaseTest extends BaseTestTemplate<StringAssert, String> {

  protected Comparables comparables;
  protected Strings strings;

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    comparables = mock(Comparables.class);
    assertions.comparables = comparables;
    strings = mock(Strings.class);
    assertions.strings = strings;
  }

  @Override
  protected StringAssert create_assertions() {
    return new StringAssert("foo");
  }

  protected Strings getStrings(StringAssert someAssertions) {
    return someAssertions.strings;
  }

  protected Comparables getComparables(StringAssert someAssertions) {
    return someAssertions.comparables;
  }

}
