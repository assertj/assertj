/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.atomic.longadder;

import static java.util.Comparator.comparingLong;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;
import java.util.concurrent.atomic.LongAdder;
import org.assertj.core.api.LongAdderAssert;
import org.assertj.core.api.LongAdderAssertBaseTest;

class LongAdderAssert_usingComparator_Test extends LongAdderAssertBaseTest {

  private final Comparator<LongAdder> comparator = comparingLong(LongAdder::longValue);

  @Override
  protected LongAdderAssert invoke_api_method() {
    // in that, we don't care of the comparator, the point to check is that we switch correctly of comparator
    return assertions.usingComparator(comparator);
  }

  @Override
  protected void verify_internal_effects() {
    assertThat(getObjects(assertions).getComparator()).isSameAs(comparator);
    assertThat(getLongs(assertions).getComparator()).isSameAs(comparator);
  }

}
