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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.api.junit.jupiter;

import java.util.List;

import org.assertj.core.api.AbstractSoftAssertions;
import org.assertj.core.api.IntegerAssert;
import org.assertj.core.api.ListAssert;

class CustomSoftAssertions extends AbstractSoftAssertions {
  public IntegerAssert expectThat(int value) {
    return proxy(IntegerAssert.class, Integer.class, value);
  }

  @SuppressWarnings("unchecked")
  public <T> ListAssert<T> expectThat(List<? extends T> actual) {
    return proxy(ListAssert.class, List.class, actual);
  }
}