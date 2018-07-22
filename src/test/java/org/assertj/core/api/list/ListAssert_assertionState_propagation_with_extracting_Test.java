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
package org.assertj.core.api.list;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.GroupAssertTestHelper.comparatorForElementFieldsWithNamesOf;
import static org.assertj.core.api.GroupAssertTestHelper.comparatorForElementFieldsWithTypeOf;
import static org.assertj.core.api.GroupAssertTestHelper.comparatorsByTypeOf;
import static org.assertj.core.api.GroupAssertTestHelper.firstNameFunction;
import static org.assertj.core.api.GroupAssertTestHelper.lastNameFunction;
import static org.assertj.core.api.GroupAssertTestHelper.throwingFirstNameExtractor;
import static org.assertj.core.extractor.Extractors.byName;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS_STRING;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS_TIMESTAMP;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS_TUPLE;
import static org.assertj.core.util.Lists.newArrayList;

import java.sql.Timestamp;
import java.util.List;

import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.groups.Tuple;
import org.assertj.core.test.Employee;
import org.assertj.core.test.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ListAssert_assertionState_propagation_with_extracting_Test {

  private Employee yoda;
  private Employee luke;
  private List<Employee> jedis;

  @BeforeEach
  public void setUp() {
    yoda = new Employee(1L, new Name("Yoda"), 800);
    luke = new Employee(2L, new Name("Luke", "Skywalker"), 26);
    jedis = newArrayList(yoda, luke);
  }

  @Test
  public void extracting_by_several_functions_should_keep_assertion_state() {
    // WHEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    AbstractListAssert<?, ?, ?, ?> assertion = assertThat(jedis).as("test description")
                                                                .withFailMessage("error message")
                                                                .withRepresentation(UNICODE_REPRESENTATION)
                                                                .usingComparatorForElementFieldsWithNames(ALWAY_EQUALS_STRING,
                                                                                                          "foo")
                                                                .usingComparatorForElementFieldsWithType(ALWAY_EQUALS_TIMESTAMP,
                                                                                                         Timestamp.class)
                                                                .usingComparatorForType(ALWAY_EQUALS_TUPLE, Tuple.class)
                                                                .extracting(firstNameFunction, lastNameFunction)
                                                                .contains(tuple("YODA", null), tuple("Luke", "Skywalker"));
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
    assertThat(comparatorsByTypeOf(assertion).get(Tuple.class)).isSameAs(ALWAY_EQUALS_TUPLE);
    assertThat(comparatorForElementFieldsWithTypeOf(assertion).get(Timestamp.class)).isSameAs(ALWAY_EQUALS_TIMESTAMP);
    assertThat(comparatorForElementFieldsWithNamesOf(assertion).get("foo")).isSameAs(ALWAY_EQUALS_STRING);
  }

  @Test
  public void extracting_by_name_should_keep_assertion_state() {
    // WHEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    AbstractListAssert<?, ?, ?, ?> assertion = assertThat(jedis).as("test description")
                                                                .withFailMessage("error message")
                                                                .withRepresentation(UNICODE_REPRESENTATION)
                                                                .usingComparatorForElementFieldsWithNames(ALWAY_EQUALS_STRING,
                                                                                                          "foo")
                                                                .usingComparatorForElementFieldsWithType(ALWAY_EQUALS_TIMESTAMP,
                                                                                                         Timestamp.class)
                                                                .usingComparatorForType(ALWAY_EQUALS_STRING, String.class)
                                                                .extracting("name.first")
                                                                .contains("YODA", "Luke");
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
    assertThat(comparatorsByTypeOf(assertion).get(String.class)).isSameAs(ALWAY_EQUALS_STRING);
    assertThat(comparatorForElementFieldsWithTypeOf(assertion).get(Timestamp.class)).isSameAs(ALWAY_EQUALS_TIMESTAMP);
    assertThat(comparatorForElementFieldsWithNamesOf(assertion).get("foo")).isSameAs(ALWAY_EQUALS_STRING);
  }

  @Test
  public void extracting_by_strongly_typed_name_should_keep_assertion_state() {
    // WHEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    AbstractListAssert<?, ?, ?, ?> assertion = assertThat(jedis).as("test description")
                                                                .withFailMessage("error message")
                                                                .withRepresentation(UNICODE_REPRESENTATION)
                                                                .usingComparatorForElementFieldsWithNames(ALWAY_EQUALS_STRING,
                                                                                                          "foo")
                                                                .usingComparatorForElementFieldsWithType(ALWAY_EQUALS_TIMESTAMP,
                                                                                                         Timestamp.class)
                                                                .usingComparatorForType(ALWAY_EQUALS_STRING, String.class)
                                                                .extracting("name.first", String.class)
                                                                .contains("YODA", "Luke");
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
    assertThat(comparatorsByTypeOf(assertion).get(String.class)).isSameAs(ALWAY_EQUALS_STRING);
    assertThat(comparatorForElementFieldsWithTypeOf(assertion).get(Timestamp.class)).isSameAs(ALWAY_EQUALS_TIMESTAMP);
    assertThat(comparatorForElementFieldsWithNamesOf(assertion).get("foo")).isSameAs(ALWAY_EQUALS_STRING);
  }

  @Test
  public void extracting_by_multiple_names_should_keep_assertion_state() {
    // WHEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    AbstractListAssert<?, ?, ?, ?> assertion = assertThat(jedis).as("test description")
                                                                .withFailMessage("error message")
                                                                .withRepresentation(UNICODE_REPRESENTATION)
                                                                .usingComparatorForElementFieldsWithNames(ALWAY_EQUALS_STRING,
                                                                                                          "foo")
                                                                .usingComparatorForElementFieldsWithType(ALWAY_EQUALS_TIMESTAMP,
                                                                                                         Timestamp.class)
                                                                .usingComparatorForType(ALWAY_EQUALS_TUPLE, Tuple.class)
                                                                .extracting("name.first", "name.last")
                                                                .contains(tuple("YODA", null), tuple("Luke", "Skywalker"));
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
    assertThat(comparatorsByTypeOf(assertion).get(Tuple.class)).isSameAs(ALWAY_EQUALS_TUPLE);
    assertThat(comparatorForElementFieldsWithTypeOf(assertion).get(Timestamp.class)).isSameAs(ALWAY_EQUALS_TIMESTAMP);
    assertThat(comparatorForElementFieldsWithNamesOf(assertion).get("foo")).isSameAs(ALWAY_EQUALS_STRING);
  }

  @Test
  public void extracting_by_single_extractor_should_keep_assertion_state() {
    // WHEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    AbstractListAssert<?, ?, ?, ?> assertion = assertThat(jedis).as("test description")
                                                                .withFailMessage("error message")
                                                                .withRepresentation(UNICODE_REPRESENTATION)
                                                                .usingComparatorForElementFieldsWithNames(ALWAY_EQUALS_STRING,
                                                                                                          "foo")
                                                                .usingComparatorForElementFieldsWithType(ALWAY_EQUALS_TIMESTAMP,
                                                                                                         Timestamp.class)
                                                                .usingComparatorForType(ALWAY_EQUALS_STRING, String.class)
                                                                .extracting(byName("name.first"))
                                                                .contains("YODA", "Luke");
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
    assertThat(comparatorsByTypeOf(assertion).get(String.class)).isSameAs(ALWAY_EQUALS_STRING);
    assertThat(comparatorForElementFieldsWithTypeOf(assertion).get(Timestamp.class)).isSameAs(ALWAY_EQUALS_TIMESTAMP);
    assertThat(comparatorForElementFieldsWithNamesOf(assertion).get("foo")).isSameAs(ALWAY_EQUALS_STRING);
  }

  @Test
  public void extracting_by_throwing_extractor_should_keep_assertion_state() {
    // WHEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    AbstractListAssert<?, ?, ?, ?> assertion = assertThat(jedis).as("test description")
                                                                .withFailMessage("error message")
                                                                .withRepresentation(UNICODE_REPRESENTATION)
                                                                .usingComparatorForElementFieldsWithNames(ALWAY_EQUALS_STRING,
                                                                                                          "foo")
                                                                .usingComparatorForElementFieldsWithType(ALWAY_EQUALS_TIMESTAMP,
                                                                                                         Timestamp.class)
                                                                .usingComparatorForType(ALWAY_EQUALS_STRING, String.class)
                                                                .extracting(throwingFirstNameExtractor)
                                                                .contains("YODA", "Luke");
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
    assertThat(comparatorsByTypeOf(assertion).get(String.class)).isSameAs(ALWAY_EQUALS_STRING);
    assertThat(comparatorForElementFieldsWithTypeOf(assertion).get(Timestamp.class)).isSameAs(ALWAY_EQUALS_TIMESTAMP);
    assertThat(comparatorForElementFieldsWithNamesOf(assertion).get("foo")).isSameAs(ALWAY_EQUALS_STRING);
  }

}
