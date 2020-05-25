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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.recursive.comparison.Color.GREEN;
import static org.assertj.core.test.NeverEqualComparator.NEVER_EQUALS_STRING;

import org.assertj.core.api.RecursiveComparisonAssert_isNotEqualTo_BaseTest;
import org.assertj.core.internal.objects.data.Person;
import org.assertj.core.test.CartoonCharacter;
import org.assertj.core.test.Jedi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("RecursiveComparisonAssert isNotEqualTo")
public class RecursiveComparisonAssert_isNotEqualTo_Test extends RecursiveComparisonAssert_isNotEqualTo_BaseTest {

  @Test
  public void should_pass_when_either_actual_or_expected_is_null() {
    // GIVEN
    Person actual = null;
    Person other = new Person();
    // THEN
    assertThat(actual).usingRecursiveComparison()
                      .isNotEqualTo(other);
    assertThat(other).usingRecursiveComparison()
                     .isNotEqualTo(actual);
  }

  @Test
  public void should_fail_when_actual_and_expected_are_null() {
    // GIVEN
    Person actual = null;
    Person other = null;
    // WHEN
    areNotEqualRecursiveComparisonFailsAsExpected(actual, other);
    // THEN
    verifyShouldNotBeEqualComparingFieldByFieldRecursivelyCall(actual, other);
  }

  @Test
  public void should_pass_when_expected_is_an_enum_and_actual_is_not() {
    // GIVEN
    RecursiveComparisonAssert_isEqualTo_Test.LightString actual = new RecursiveComparisonAssert_isEqualTo_Test.LightString("GREEN");
    Light other = new Light(GREEN);
    // THEN
    assertThat(actual).usingRecursiveComparison()
                      .isNotEqualTo(other);
  }

  @Test
  public void should_fail_when_field_values_are_null() {
    // GIVEN
    Jedi actual = new Jedi("Yoda", null);
    Jedi other = new Jedi("Yoda", null);
    recursiveComparisonConfiguration.ignoreFields("name");
    // WHEN
    areNotEqualRecursiveComparisonFailsAsExpected(actual, other);
    // THEN
    verifyShouldNotBeEqualComparingFieldByFieldRecursivelyCall(actual, other);
  }

  @Test
  public void should_fail_when_fields_are_equal_even_if_objects_types_differ() {
    // GIVEN
    CartoonCharacter actual = new CartoonCharacter("Homer Simpson");
    Person other = new Person("Homer Simpson");
    recursiveComparisonConfiguration.ignoreFields("children");
    // WHEN
    areNotEqualRecursiveComparisonFailsAsExpected(actual, other);
    // THEN
    verifyShouldNotBeEqualComparingFieldByFieldRecursivelyCall(actual, other);
  }

  @Test
  public void should_fail_when_all_field_values_equal() {
    // GIVEN
    Jedi actual = new Jedi("Yoda", "Green");
    Jedi other = new Jedi("Luke", "Green");
    recursiveComparisonConfiguration.ignoreFields("name");
    //
    areNotEqualRecursiveComparisonFailsAsExpected(actual, other);
    // THEN
    verifyShouldNotBeEqualComparingFieldByFieldRecursivelyCall(actual, other);
  }

  @Test
  public void should_fail_when_all_field_values_equal_and_no_fields_are_ignored() {
    // GIVEN
    Jedi actual = new Jedi("Yoda", "Green");
    Jedi other = new Jedi("Yoda", "Green");
    // WHEN
    areNotEqualRecursiveComparisonFailsAsExpected(actual, other);
    // THEN
    verifyShouldNotBeEqualComparingFieldByFieldRecursivelyCall(actual, other);

  }

  @Test
  public void should_be_able_to_compare_objects_of_different_types() {
    // GIVEN
    CartoonCharacter other = new CartoonCharacter("Homer Simpson");
    Person actual = new Person("Homer Simpson");
    // THEN not equal because of children field in CartoonCharacter
    assertThat(actual).usingRecursiveComparison()
                      .isNotEqualTo(other);
  }

  @Test
  public void should_be_able_to_use_a_comparator_for_specified_type() {
    // GIVEN
    Jedi actual = new Jedi("Yoda", "Green");
    Jedi other = new Jedi(new String("Yoda"), "Green");
    // THEN
    assertThat(actual).usingRecursiveComparison()
                      .withComparatorForType(NEVER_EQUALS_STRING, String.class)
                      .isNotEqualTo(other);
  }

  @Test
  public void should_pass_when_one_property_or_field_to_compare_can_not_be_found() {
    // GIVEN
    Jedi actual = new Jedi("Yoda", "Green");
    Person other = new Person("Yoda");
    other.neighbour = other;
    // THEN
    assertThat(actual).usingRecursiveComparison()
                      .isNotEqualTo(other);
  }

  @Test
  public void should_be_able_to_use_a_comparator_for_specified_fields() {
    // GIVEN
    Jedi actual = new Jedi("Yoda", "Green");
    Jedi other = new Jedi("Yoda", new String("Green"));
    // THEN
    assertThat(actual).usingRecursiveComparison()
                      .withComparatorForFields(NEVER_EQUALS_STRING, "lightSaberColor")
                      .isNotEqualTo(other);
  }

}
