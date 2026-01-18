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
package org.example.soft;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SoftAssertionsExtension.class)
class SoftAssertionsFieldInjectionExample {

  // initialized by the SoftlyExtension extension
  @InjectSoftAssertions
  private SoftAssertions soft;

  @Test
  public void chained_soft_assertions_example() {
    String name = "Michael Jordan - Bulls";
    soft.assertThat(name)
        .startsWith("Mi")
        .contains("Bulls");
    // no need to call softly.assertAll(), this is done by the extension
  }

  // nested classes test work too
  @Nested
  class NestedExample {

    @Test
    public void football_assertions_example() {
      String kylian = "Kylian Mbappé";
      soft.assertThat(kylian)
          .startsWith("Ky")
          .contains("bap");
      // no need to call softly.assertAll(), this is done by the extension
    }
  }
}
