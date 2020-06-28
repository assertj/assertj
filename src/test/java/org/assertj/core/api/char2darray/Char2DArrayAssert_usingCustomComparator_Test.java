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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api.char2darray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.AlwaysEqualComparator.alwaysEqual;

import org.assertj.core.api.Char2DArrayAssert;
import org.assertj.core.api.Char2DArrayAssertBaseTest;
import org.assertj.core.test.AlwaysEqualComparator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Char2DArrayAssert usingCustomComparator")
class Char2DArrayAssert_usingCustomComparator_Test extends Char2DArrayAssertBaseTest {

  private static final AlwaysEqualComparator<char[][]> ALWAYS_EQUAL = alwaysEqual();

  @Override
  protected Char2DArrayAssert invoke_api_method() {
    return assertions.usingComparator(ALWAYS_EQUAL);
  }

  @Override
  protected void verify_internal_effects() {
    assertThat(getObjects(assertions).getComparator()).isSameAs(ALWAYS_EQUAL);
  }

  @Test
  public void should_honor_comparator() {
    assertThat(new char[][] {}).usingComparator(ALWAYS_EQUAL)
                               .isEqualTo(new char[][] { { 'a', 'b' }, { 'c', 'd' } });
  }

}
