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
package example.core;

import org.assertj.core.api.AutoCloseableBDDSoftAssertions;
import org.junit.jupiter.api.Test;

public class AutoCloseableBDDSoftAssertionsExample {

  // tag::closeable-bdd-soft-assertions[]
  @Test
  void auto_closeable_bdd_soft_assertions_example() {
    try (AutoCloseableBDDSoftAssertions softly = new AutoCloseableBDDSoftAssertions()) {
      softly.then("George Martin").as("great authors").isEqualTo("JRR Tolkien");
      softly.then(42).as("response to Everything").isGreaterThan(100);
      softly.then("Gandalf").isEqualTo("Sauron");
      // no need to call assertAll, this is done when softly is closed.
    }
  }
  // end::closeable-bdd-soft-assertions[]

}
