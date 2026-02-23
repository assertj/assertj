/*
 * Copyright 2012-2026 the original author or authors.
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
package org.assertj.tests.core.kotlin.api.object_

import org.assertj.core.api.Assertions.assertThatObject
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class ObjectAssert_extracting_with_Function_Test {

  @Test
  fun `should support lambda with implicit parameter`() {
    // WHEN/THEN
    assertThatObject(" ").extracting { it.trim() }.isEqualTo("")
  }

  @Test
  fun `should support lambda with explicit parameter`() {
    // WHEN/THEN
    assertThatObject(" ").extracting { it -> it.trim() }.isEqualTo("")
  }

  @Disabled("Does not compile with Kotlin < 2.x")
  @Test
  fun `should support lambda with implicit parameter on two chained calls`() {
/*
    // WHEN/THEN
    assertThatObject(" ")
      .extracting { it.trim() }
      .isEqualTo("")
      .extracting { it.isEmpty() }
      .isEqualTo(true)
*/
  }

  @Test
  fun `should support method reference`() {
    // WHEN/THEN
    assertThatObject(" ").extracting(String::trim).isEqualTo("")
  }

  @Disabled("Does not compile with Kotlin < 2.x")
  @Test
  fun `should support method reference on two chained calls`() {
/*
    // WHEN/THEN
    assertThatObject(" ")
      .extracting(String::trim)
      .isEqualTo("")
      .extracting(String::isEmpty)
      .isEqualTo(true)
*/
  }

}
