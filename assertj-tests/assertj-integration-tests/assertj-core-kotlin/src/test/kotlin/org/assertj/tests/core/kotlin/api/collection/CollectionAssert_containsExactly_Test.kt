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
package org.assertj.tests.core.kotlin.api.collection

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CollectionAssert_containsExactly_Test {

  // https://github.com/assertj/assertj/issues/2439
  @Test
  fun should_work_with_kotlin_set() {
    // GIVEN
    val set = setOf("value")
    // WHEN/THEN
    assertThat(set).containsExactly("value")
    assertThat(set).describedAs("A set").containsExactly("value")
  }

}
