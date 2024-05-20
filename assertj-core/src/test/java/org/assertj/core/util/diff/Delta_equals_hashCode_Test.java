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
package org.assertj.core.util.diff;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.testkit.EqualsHashCodeContractAssert.assertEqualsIsReflexive;
import static org.assertj.core.testkit.EqualsHashCodeContractAssert.assertEqualsIsSymmetric;
import static org.assertj.core.testkit.EqualsHashCodeContractAssert.assertEqualsIsTransitive;
import static org.assertj.core.testkit.EqualsHashCodeContractAssert.assertMaintainsEqualsAndHashCodeContract;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Delta_equals_hashCode_Test {

  private Chunk<String> chunk;
  private Delta<String> delta;

  @BeforeEach
  public void setUp() {
    chunk = new Chunk<>(1, Collections.emptyList());
    delta = new ChangeDelta<>(chunk, chunk);
  }

  @Test
  void should_have_reflexive_equals() {
    assertEqualsIsReflexive(delta);
  }

  @Test
  void should_have_symmetric_equals() {
    assertEqualsIsSymmetric(delta, new ChangeDelta<>(chunk, chunk));
  }

  @Test
  void should_have_transitive_equals() {
    assertEqualsIsTransitive(delta, new ChangeDelta<>(chunk, chunk), new ChangeDelta<>(chunk, chunk));
  }

  @Test
  void should_maintain_equals_and_hashCode_contract() {
    assertMaintainsEqualsAndHashCodeContract(delta, new ChangeDelta<>(chunk, chunk));
  }

  @Test
  void should_not_be_equal_to_Object_of_different_type() {
    assertThat(delta.equals("8")).isFalse();
  }

  @Test
  void should_not_be_equal_to_null() {
    assertThat(delta.equals(null)).isFalse();
  }

  @Test
  void should_not_be_equal_to_Delta_with_different_value() {
    Chunk<String> chunk2 = new Chunk<>(5, Collections.emptyList());
    assertThat(delta.equals(new ChangeDelta<>(chunk2, chunk2))).isFalse();
  }
}
