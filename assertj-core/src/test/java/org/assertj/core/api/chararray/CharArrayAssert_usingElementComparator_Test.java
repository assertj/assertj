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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api.chararray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.testkit.AlwaysEqualComparator.alwaysEqual;

import java.util.Comparator;

import org.assertj.core.api.CharArrayAssert;
import org.assertj.core.api.CharArrayAssertBaseTest;
import org.assertj.core.internal.Objects;
import org.junit.jupiter.api.BeforeEach;

/**
 * Tests for <code>{@link CharArrayAssert#usingElementComparator(Comparator)}</code>.
 *
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
class CharArrayAssert_usingElementComparator_Test extends CharArrayAssertBaseTest {

  private Comparator<Character> comparator = alwaysEqual();

  private Objects objectsBefore;

  @BeforeEach
  void before() {
    objectsBefore = getObjects(assertions);
  }

  @Override
  protected CharArrayAssert invoke_api_method() {
    // in that test, the comparator type is not important, we only check that we correctly switch of comparator
    return assertions.usingElementComparator(comparator);
  }

  @Override
  protected void verify_internal_effects() {
    assertThat(getObjects(assertions)).isSameAs(objectsBefore);
    assertThat(getArrays(assertions).getComparator()).isSameAs(comparator);
  }
}
