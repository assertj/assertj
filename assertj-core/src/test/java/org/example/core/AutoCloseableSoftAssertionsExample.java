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

import org.junit.jupiter.api.Test;
import org.assertj.core.api.AutoCloseableBDDSoftAssertions;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.assertj.core.api.BDDSoftAssertions;
import org.assertj.core.api.SoftAssertions;

public class AutoCloseableSoftAssertionsExample {

  // tag::closeable-soft-assertions[]
  @Test
  void auto_closeable_soft_assertions_example() {
    try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
      softly.assertThat("George Martin").as("great authors").isEqualTo("JRR Tolkien");  // <2>
      softly.assertThat(42).as("response to Everything").isGreaterThan(100); // <2>
      softly.assertThat("Gandalf").isEqualTo("Sauron"); // <2>
      // no need to call assertAll, this is done when softly is closed.
    }
  }
  // end::closeable-soft-assertions[]

}
