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

import org.assertj.core.internal.Comparables;

public abstract class AbstractGenericComparableAssertBaseTest
    extends BaseTestTemplate<GenericComparableAssertV2<String>, Comparable<String>> {

  protected Comparables comparables;

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    comparables = mock(Comparables.class);
    assertions.comparables = comparables;
  }

  @Override
  protected GenericComparableAssertV2<String> create_assertions() {
    return new GenericComparableAssertV2<>("abc");
  }

  protected Comparables getComparables(GenericComparableAssertV2<?> someAssertions) {
    return someAssertions.comparables;
  }

}
