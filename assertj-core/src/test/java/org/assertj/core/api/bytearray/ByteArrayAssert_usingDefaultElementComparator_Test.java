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
package org.assertj.core.api.bytearray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.testkit.AlwaysEqualComparator.alwaysEqual;

import org.assertj.core.api.ByteArrayAssert;
import org.assertj.core.api.ByteArrayAssertBaseTest;
import org.assertj.core.internal.ByteArrays;
import org.assertj.core.internal.Objects;
import org.junit.jupiter.api.BeforeEach;

/**
 * Tests for <code>{@link ByteArrayAssert#usingDefaultElementComparator()}</code>.
 *
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
class ByteArrayAssert_usingDefaultElementComparator_Test extends ByteArrayAssertBaseTest {

  private Objects objectsBefore;

  @BeforeEach
  void before() {
    objectsBefore = getObjects(assertions);
  }

  @Override
  protected ByteArrayAssert invoke_api_method() {
    return assertions.usingElementComparator(alwaysEqual())
                     .usingDefaultElementComparator();
  }

  @Override
  protected void verify_internal_effects() {
    assertThat(getArrays(assertions)).isSameAs(ByteArrays.instance());
    assertThat(getArrays(assertions).getComparator()).isNull();
    assertThat(getObjects(assertions)).isSameAs(objectsBefore);
  }
}
