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
package org.assertj.core.api.shortarray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.testkit.AlwaysEqualComparator.alwaysEqual;

import org.assertj.core.api.ShortArrayAssert;
import org.assertj.core.api.ShortArrayAssertBaseTest;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.ShortArrays;
import org.junit.jupiter.api.BeforeEach;

/**
 * Tests for <code>{@link ShortArrayAssert#usingDefaultElementComparator()}</code>.
 *
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
class ShortArrayAssert_usingDefaultElementComparator_Test extends ShortArrayAssertBaseTest {

  private Objects objectsBefore;

  @BeforeEach
  void before() {
    objectsBefore = getObjects(assertions);
  }

  @Override
  protected ShortArrayAssert invoke_api_method() {
    return assertions.usingElementComparator(alwaysEqual())
                     .usingDefaultElementComparator();
  }

  @Override
  protected void verify_internal_effects() {
    assertThat(getObjects(assertions)).isSameAs(objectsBefore);
    assertThat(getArrays(assertions).getComparator()).isNull();
    assertThat(getArrays(assertions)).isSameAs(ShortArrays.instance());
  }
}
