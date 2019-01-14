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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.AlwaysEqualComparator.alwaysEqual;

import org.assertj.core.test.AlwaysEqualComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FieldComparators_registerComparator_Test {

  private FieldComparators fieldComparators;

  @BeforeEach
  public void setup() {
    fieldComparators = new FieldComparators();
  }

  @Test
  public void should_register_comparator_for_a_given_fieldLocation() {
    // GIVEN
    FieldLocation fooLocation = new FieldLocation("foo");
    // WHEN
    AlwaysEqualComparator<?> alwaysEqualComparator = alwaysEqual();
    fieldComparators.registerComparator(fooLocation, alwaysEqualComparator);
    // THEN
    assertThat(fieldComparators.hasComparatorForField(fooLocation)).isTrue();
    assertThat(fieldComparators.getComparatorForField(fooLocation)).isSameAs(alwaysEqualComparator);
  }

  @Test
  public void hasComparatorForField_should_return_false_for_field_location_without_comparator() {
    // GIVEN
    FieldLocation fooLocation = new FieldLocation("foo");
    // THEN
    assertThat(fieldComparators.hasComparatorForField(fooLocation)).isFalse();
  }

  @Test
  public void getComparatorForField_should_return_null_for_field_location_without_comparator() {
    // GIVEN
    FieldLocation fooLocation = new FieldLocation("foo");
    // THEN
    assertThat(fieldComparators.getComparatorForField(fooLocation)).isNull();
  }

}
