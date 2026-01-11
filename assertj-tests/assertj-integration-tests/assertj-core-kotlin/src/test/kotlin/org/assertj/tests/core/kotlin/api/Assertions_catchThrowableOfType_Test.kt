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
package org.assertj.tests.core.kotlin.api

import org.assertj.core.api.Assertions.catchThrowableOfType
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test

class Assertions_catchThrowableOfType_Test {

  @Test
  fun `should compile with lambda expressions`() {
    // GIVEN
    val exception = Exception("boom!!")
    // WHEN
    val thrown = catchThrowableOfType(Exception::class.java) {
      throw exception
    }
    // THEN
    then(thrown).isSameAs(exception)
  }

}
