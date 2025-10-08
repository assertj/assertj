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

class CollectionAssert_anyMatch_Test {

  // https://youtrack.jetbrains.com/issue/KT-53113/
  @Test
  fun should_work_with_kotlin_collection() {
    // GIVEN
    data class Person(val name: String, val age: Int)
    val collection: Collection<Person> = listOf(Person("Donald", 33), Person("Daisy", 30))
    // WHEN/THEN
    assertThat(collection).anyMatch { it.age > 30 }
    assertThat(collection).describedAs("Older than 30").anyMatch { it.age > 30 }
  }

}
