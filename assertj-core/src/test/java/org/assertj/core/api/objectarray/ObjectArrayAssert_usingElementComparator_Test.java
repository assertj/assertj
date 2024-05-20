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
package org.assertj.core.api.objectarray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.testkit.AlwaysEqualComparator.alwaysEqual;

import java.util.Comparator;
import org.assertj.core.api.ObjectArrayAssert;
import org.assertj.core.api.ObjectArrayAssertBaseTest;

/**
 * Tests for <code>{@link ObjectArrayAssert#usingElementComparator(Comparator)}</code>.
 *
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
class ObjectArrayAssert_usingElementComparator_Test extends ObjectArrayAssertBaseTest {

  private Comparator<Object> elementComparator = alwaysEqual();

  @Override
  protected ObjectArrayAssert<Object> invoke_api_method() {
    return assertions.usingElementComparator(elementComparator);
  }

  @Override
  protected void verify_internal_effects() {
    assertThat(getArrays(assertions).getComparator()).isSameAs(elementComparator);
  }

}
