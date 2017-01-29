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
package org.assertj.core.data;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.test.EqualsHashCodeContractAssert.assertEqualsIsReflexive;
import static org.assertj.core.test.EqualsHashCodeContractAssert.assertEqualsIsSymmetric;
import static org.assertj.core.test.EqualsHashCodeContractAssert.assertEqualsIsTransitive;
import static org.assertj.core.test.EqualsHashCodeContractAssert.assertMaintainsEqualsAndHashCodeContract;

import java.util.Map.Entry;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for {@link MapEntry#equals(Object)} and {@link MapEntry#hashCode()}.
 *
 * @author Alex Ruiz
 */
public class MapEntry_equals_hashCode_Test {
  private static MapEntry<String, String> entry;
  private static Entry<String, String> javaEntry;

  @BeforeClass
  public static void setUpOnce() {
    entry = entry("key", "value");
    javaEntry = singletonMap("key", "value").entrySet().iterator().next();
  }

  @Test
  public void should_have_reflexive_equals() {
    assertEqualsIsReflexive(entry);
  }

  @Test
  public void should_have_symmetric_equals() {
    assertEqualsIsSymmetric(entry, entry("key", "value"));
  }

  @Test
  public void should_have_transitive_equals() {
    assertEqualsIsTransitive(entry, entry("key", "value"), entry("key", "value"));
  }

  @Test
  public void should_maintain_equals_and_hashCode_contract() {
    assertMaintainsEqualsAndHashCodeContract(entry, entry("key", "value"));
  }

  @Test
  public void should_not_be_equal_to_Object_of_different_type() {
    assertThat(entry.equals("{'key', 'value'}")).isFalse();
  }

  @Test
  public void should_not_be_equal_to_null() {
    assertThat(entry.equals(null)).isFalse();
  }

  @Test
  public void should_not_be_equal_to_MapEntry_with_different_value() {
    assertThat(entry.equals(entry("key0", "value0"))).isFalse();
  }

  @Test
  public void should_have_symmetric_equals_with_java_MapEntry() {
    assertEqualsIsSymmetric(javaEntry, entry);
  }

  @Test
  public void should_maintain_equals_and_hashCode_contract_with_java_MapEntry() {
    assertMaintainsEqualsAndHashCodeContract(javaEntry, entry);
  }

  @Test
  public void should_have_transitive_equals_with_java_MapEntry() {
    Entry<String, String> javaEntry2 = singletonMap("key", "value").entrySet().iterator().next();
    assertEqualsIsTransitive(entry, javaEntry, javaEntry2);
  }
}
