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
package org.assertj.core.api.integer_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.testkit.AlwaysEqualComparator.alwaysEqual;

import org.assertj.core.api.IntegerAssert;
import org.assertj.core.api.IntegerAssertBaseTest;
import org.assertj.core.internal.Integers;
import org.assertj.core.internal.Objects;

/**
 * Tests for <code>{@link IntegerAssert#usingDefaultComparator()}</code>.
 *
 * @author Joel Costigliola
 */
class IntegerAssert_usingDefaultComparator_Test extends IntegerAssertBaseTest {

  @Override
  protected IntegerAssert invoke_api_method() {
    return assertions.usingComparator(alwaysEqual())
                     .usingDefaultComparator();
  }

  @Override
  protected void verify_internal_effects() {
    assertThat(getObjects(assertions)).isSameAs(Objects.instance());
    assertThat(getObjects(assertions).getComparator()).isNull();
    assertThat(getIntegers(assertions)).isSameAs(Integers.instance());
  }
}
