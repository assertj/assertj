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
package org.example.core;

// tag::user_guide[]
import static org.assertj.core.api.Assertions.assertThat; // <1>

import org.junit.jupiter.api.Test;

public class SimpleAssertionsExample {

  @Test
  void a_few_simple_assertions() {
    assertThat("The Lord of the Rings").isNotNull()  // <2> <3>
                                       .startsWith("The") // <4>
                                       .contains("Lord") // <4>
                                       .endsWith("Rings"); // <4>
  }

}
// end::user_guide[]
