/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.booleanarray;

import static org.assertj.core.testkit.AlwaysEqualComparator.alwaysEqual;
import static org.mockito.Mockito.verify;

import java.util.Comparator;
import org.assertj.core.api.BooleanArrayAssert;
import org.assertj.core.api.BooleanArrayAssertBaseTest;

/**
 * Tests for <code>{@link BooleanArrayAssert#isSortedAccordingTo(Comparator)}</code>.
 *
 * @author Joel Costigliola
 */
class BooleanArrayAssert_isSortedAccordingToComparator_Test extends BooleanArrayAssertBaseTest {

  private Comparator<Boolean> comparator = alwaysEqual();

  @Override
  protected BooleanArrayAssert invoke_api_method() {
    return assertions.isSortedAccordingTo(comparator);
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertIsSortedAccordingToComparator(getInfo(assertions), getActual(assertions), comparator);
  }

}
