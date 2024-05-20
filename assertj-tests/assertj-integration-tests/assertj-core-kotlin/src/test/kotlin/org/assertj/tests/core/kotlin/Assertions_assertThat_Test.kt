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
package org.assertj.tests.core.kotlin

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class Assertions_assertThat_Test {

  @Test
  fun intarray() {
    val x: IntArray = intArrayOf(1, 2, 3)
    assertThat(x).contains(1, 2, 3)
  }

  @Test
  fun `immutable list`() {
    val list = listOf("Viserys", "Rhaenyra", "Daemon")
    assertThat(list).contains("Viserys", "Rhaenyra", "Daemon")
    assertThat(list).hasSize(3).anySatisfy {
      assertThat(it).isNotEqualTo("Corlys")
      assertThat(it).hasSize(6)
    }
  }

  @Test
  fun `mutable list`() {
    val list = mutableListOf("Viserys", "Rhaenyra", "Daemon")
    assertThat(list).contains("Viserys", "Rhaenyra", "Daemon")
  }

}
