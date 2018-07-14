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
package org.assertj.core.api.iterable;

import static java.util.Comparator.comparing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.GroupAssertTestHelper.comparatorForElementFieldsWithNamesOf;
import static org.assertj.core.api.GroupAssertTestHelper.comparatorForElementFieldsWithTypeOf;
import static org.assertj.core.api.GroupAssertTestHelper.comparatorsByTypeOf;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS_STRING;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS_TIMESTAMP;
import static org.assertj.core.test.ExpectedException.none;

import java.sql.Timestamp;
import java.util.SortedSet;
import java.util.TreeSet;

import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.test.ExpectedException;
import org.assertj.core.test.FluentJedi;
import org.assertj.core.test.Name;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for:
 * <ul>
 *  <li><code>{@link AbstractIterableAssert#extractingResultOf(String)}</code>,
 *  <li><code>{@link AbstractIterableAssert#extractingResultOf(String, Class)}</code>.
 * </ul>
 *
 * @author Michał Piotrkowski
 */
public class IterableAssert_extractingResultOf_with_SortedSet_Test {

  private static FluentJedi yoda;
  private static FluentJedi vader;
  private static Iterable<FluentJedi> jedis;

  @BeforeClass
  public static void setUpOnce() {
    yoda = new FluentJedi(new Name("Yoda"), 800, false);
    vader = new FluentJedi(new Name("Darth Vader"), 50, true);
    jedis = newSortedSet(yoda, vader);
  }

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_allow_assertions_on_method_invocation_result_extracted_from_given_iterable() {
    // extract method result
    assertThat(jedis).extractingResultOf("age").containsOnly(800, 50);
    // extract if method result is primitive
    assertThat(jedis).extractingResultOf("darkSide").containsOnly(false, true);
    // extract if method result is also a property
    assertThat(jedis).extractingResultOf("name").containsOnly(new Name("Yoda"), new Name("Darth Vader"));
    // extract toString method result
    assertThat(jedis).extractingResultOf("toString").containsOnly("Yoda", "Darth Vader");
  }

  @Test
  public void should_allow_assertions_on_method_invocation_result_extracted_from_given_iterable_with_enforcing_return_type() {
    assertThat(jedis).extractingResultOf("name", Name.class).containsOnly(new Name("Yoda"), new Name("Darth Vader"));
  }

  @Test
  public void should_throw_error_if_no_method_with_given_name_can_be_extracted() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(jedis).extractingResultOf("unknown"))
                                        .withMessage("Can't find method 'unknown' in class FluentJedi.class. Make sure public method exists and accepts no arguments!");
  }

  @Test
  public void should_use_method_name_as_description_when_extracting_result_of_method_list() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(jedis).extractingResultOf("age")
                                                                                      .isEmpty())
                                                   .withMessageContaining("[Extracted: result of age()]");
  }

  @Test
  public void should_use_method_name_as_description_when_extracting_typed_result_of_method_list() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(jedis).extractingResultOf("age",
                                                                                                          Integer.class)
                                                                                      .isEmpty())
                                                   .withMessageContaining("[Extracted: result of age()]");
  }

  @Test
  public void extractingResultOf_should_keep_assertion_state() {
    // WHEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    AbstractListAssert<?, ?, ?, ?> assertion = assertThat(jedis).as("test description")
                                                                .withFailMessage("error message")
                                                                .withRepresentation(UNICODE_REPRESENTATION)
                                                                .usingComparatorForElementFieldsWithNames(ALWAY_EQUALS_STRING,
                                                                                                          "foo")
                                                                .usingComparatorForElementFieldsWithType(ALWAY_EQUALS_TIMESTAMP,
                                                                                                         Timestamp.class)
                                                                .extractingResultOf("toString")
                                                                .usingComparatorForType(CaseInsensitiveStringComparator.instance,
                                                                                        String.class)
                                                                .containsOnly("YODA", "darth vader");
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
    assertThat(comparatorsByTypeOf(assertion).get(String.class)).isSameAs(CaseInsensitiveStringComparator.instance);
    assertThat(comparatorForElementFieldsWithTypeOf(assertion).get(Timestamp.class)).isSameAs(ALWAY_EQUALS_TIMESTAMP);
    assertThat(comparatorForElementFieldsWithNamesOf(assertion).get("foo")).isSameAs(ALWAY_EQUALS_STRING);
  }

  @Test
  public void strongly_typed_extractingResultOf_should_keep_assertion_state() {
    // WHEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    AbstractListAssert<?, ?, ?, ?> assertion = assertThat(jedis).as("test description")
                                                                .withFailMessage("error message")
                                                                .withRepresentation(UNICODE_REPRESENTATION)
                                                                .usingComparatorForElementFieldsWithNames(ALWAY_EQUALS_STRING,
                                                                                                          "foo")
                                                                .usingComparatorForElementFieldsWithType(ALWAY_EQUALS_TIMESTAMP,
                                                                                                         Timestamp.class)
                                                                .extractingResultOf("toString", String.class)
                                                                .usingComparatorForType(CaseInsensitiveStringComparator.instance,
                                                                                        String.class)
                                                                .containsOnly("YODA", "darth vader");
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
    assertThat(comparatorsByTypeOf(assertion).get(String.class)).isSameAs(CaseInsensitiveStringComparator.instance);
    assertThat(comparatorForElementFieldsWithTypeOf(assertion).get(Timestamp.class)).isSameAs(ALWAY_EQUALS_TIMESTAMP);
    assertThat(comparatorForElementFieldsWithNamesOf(assertion).get("foo")).isSameAs(ALWAY_EQUALS_STRING);
  }

  private static SortedSet<FluentJedi> newSortedSet(FluentJedi... jedis) {
    TreeSet<FluentJedi> jediSortedSet = new TreeSet<>(comparing(FluentJedi::age));
    for (FluentJedi cartoonCharacter : jedis) {
      jediSortedSet.add(cartoonCharacter);
    }
    return jediSortedSet;
  }

}
