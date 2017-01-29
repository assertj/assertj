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
package org.assertj.core.api.atomic.long_;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicLong;

import org.assertj.core.util.AbsValueComparator;
import org.junit.Test;

public class AtomicLongAssert_customComparator_Test {

  @Test
  public void should_honor_custom_comparator() {
    assertThat(new AtomicLong(1)).usingComparator(new AbsValueComparator<AtomicLong>()).hasValueLessThanOrEqualTo(-1);
  }
  
}
