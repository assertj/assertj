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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api.boolean_;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.BooleanAssert;
import org.assertj.core.api.BooleanAssertBaseTest;
import org.assertj.core.internal.Objects;

/**
 * Tests for <code>{@link BooleanAssert#usingComparator(java.util.Comparator)}</code> and
 * <code>{@link BooleanAssert#usingDefaultComparator()}</code>.
 *
 * @author Joel Costigliola
 */
class BooleanAssert_usingDefaultComparator_Test extends BooleanAssertBaseTest {

  @Override
  protected BooleanAssert invoke_api_method() {
    return assertions.usingDefaultComparator();
  }

  @Override
  protected void verify_internal_effects() {
    assertThat(getObjects(assertions).getComparator()).isNull();
    assertThat(getObjects(assertions)).isSameAs(Objects.instance());
  }
}
