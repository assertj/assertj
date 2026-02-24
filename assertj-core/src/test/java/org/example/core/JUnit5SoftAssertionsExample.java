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

// tag::junit5-soft-assertions[]
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.assertj.core.api.BDDSoftAssertions;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;

@ExtendWith(SoftAssertionsExtension.class)
public class JUnit5SoftAssertionsExample {

  @Test
  void junit5_soft_assertions_multiple_failures_example(SoftAssertions softly) {
    softly.assertThat("George Martin").as("great authors").isEqualTo("JRR Tolkien");
    softly.assertThat(42).as("response to Everything").isGreaterThan(100);
    softly.assertThat("Gandalf").isEqualTo("Sauron");
    // No need to call softly.assertAll(), this is automatically done by the SoftAssertionsExtension
  }

  @Test
  void junit5_bdd_soft_assertions_multiple_failures_example(BDDSoftAssertions softly) {
    softly.then("George Martin").as("great authors").isEqualTo("JRR Tolkien");
    softly.then(42).as("response to Everything").isGreaterThan(100);
    softly.then("Gandalf").isEqualTo("Sauron");
    // No need to call softly.assertAll(), this is automatically done by the SoftAssertionsExtension
  }

  @ParameterizedTest
  @CsvSource({ "1, 1, 2", "1, 2, 3" })
  // test parameters come first, soft assertion must come last.
  void junit5_soft_assertions_parameterized_test_example(int a, int b, int sum, SoftAssertions softly) {
    softly.assertThat(a + b).as("sum").isEqualTo(sum);
    softly.assertThat(a).isLessThan(sum);
    softly.assertThat(b).isLessThan(sum);
  }

}
// end::junit5-soft-assertions[]
