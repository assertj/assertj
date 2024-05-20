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
package org.assertj.tests.core.osgi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Test;

class SimpleTest {

  @Test
  void simple_success() {
    assertThat("A String").isNotNull()
                          .isNotEmpty()
                          .contains("A", "String")
                          .isEqualTo("A String");
  }

  @Test
  void simple_failure() {
    assertThatCode(() -> assertThat("A String").isNull()).isInstanceOf(AssertionError.class);
  }

}
