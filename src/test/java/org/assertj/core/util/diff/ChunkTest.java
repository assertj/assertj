/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.util.diff;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.EqualsHashCodeContractAssert.assertEqualsIsReflexive;
import static org.assertj.core.test.EqualsHashCodeContractAssert.assertEqualsIsSymmetric;
import static org.assertj.core.test.EqualsHashCodeContractAssert.assertEqualsIsTransitive;
import static org.assertj.core.test.EqualsHashCodeContractAssert.assertMaintainsEqualsAndHashCodeContract;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class ChunkTest {

  private Chunk<String> chunk;

  @Before
  public void setUp() {
    chunk = new Chunk<>(1, emptyList());
  }

  @Test
  public void should_have_reflexive_equals() {
    assertEqualsIsReflexive(chunk);
  }

  @Test
  public void should_have_symmetric_equals() {
    assertEqualsIsSymmetric(chunk, new Chunk<>(1, emptyList()));
  }

  @Test
  public void should_have_transitive_equals() {
    assertEqualsIsTransitive(chunk, new Chunk<>(1, emptyList()), new Chunk<>(1, emptyList()));
  }

  @Test
  public void should_maintain_equals_and_hashCode_contract() {
    assertMaintainsEqualsAndHashCodeContract(chunk, new Chunk<>(1, emptyList()));
  }

  @Test
  public void should_not_be_equal_to_Object_of_different_type() {
    assertThat(chunk.equals("8")).isFalse();
  }

  @Test
  public void should_not_be_equal_to_null() {
    assertThat(chunk.equals(null)).isFalse();
  }

  @Test
  public void should_not_be_equal_to_Chunk_with_different_value() {
    assertThat(chunk.equals(new Chunk<>(2, emptyList()))).isFalse();
  }

  @Test
  public void should_have_nice_toString_value() {
    assertThat(chunk).hasToString("[position: 1, size: 0, lines: []]");
  }

  private static List<String> emptyList() {
    return Collections.<String>emptyList();
  }
}
