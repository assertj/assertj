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
import org.assertj.core.api.JUnitBDDSoftAssertions;
import org.junit.Rule;

public class JUnit4BDDSoftAssertionsExample {

  // tag::junit4-bdd-soft-assertions[]
  @Rule
  public final JUnitBDDSoftAssertions softly = new JUnitBDDSoftAssertions();

  @Test
  void junit4_bdd_soft_assertions_example() {
    softly.then("George Martin").as("great authors").isEqualTo("JRR Tolkien");
    softly.then(42).as("response to Everything").isGreaterThan(100);
    softly.then("Gandalf").isEqualTo("Dauron");
    // No need to call softly.assertAll(), this is automatically done by the JUnitSoftAssertions rule
  }
  // end::junit4-bdd-soft-assertions[]

}
