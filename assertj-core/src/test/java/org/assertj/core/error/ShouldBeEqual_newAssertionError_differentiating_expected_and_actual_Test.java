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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.Integer.toHexString;
import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.Strings.concat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Comparator;

import org.assertj.core.description.Description;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

/**
 * Tests for
 * <code>{@link ShouldBeEqual#newAssertionError(Description, org.assertj.core.presentation.Representation)}</code>.
 *
 * @author Joel Costigliola (based on Tomasz Nurkiewicz ideas)
 */
class ShouldBeEqual_newAssertionError_differentiating_expected_and_actual_Test {

  private String formattedDescription = "[my test]";
  private Description description;

  @BeforeEach
  public void setUp() {
    description = new TestDescription("my test");
  }

  @Test
  void should_create_AssertionError_with_message_differentiating_expected_double_and_actual_float() {
    // GIVEN
    Float actual = 42f;
    Double expected = 42d;
    ShouldBeEqual shouldBeEqual = (ShouldBeEqual) shouldBeEqual(actual, expected, STANDARD_REPRESENTATION);
    shouldBeEqual.descriptionFormatter = mock(DescriptionFormatter.class);
    when(shouldBeEqual.descriptionFormatter.format(description)).thenReturn(formattedDescription);
    // WHEN
    AssertionError error = shouldBeEqual.newAssertionError(description, STANDARD_REPRESENTATION);
    // THEN
    then(error).isInstanceOf(AssertionFailedError.class)
               .hasMessage(format("[my test] %n" +
                                  "expected: 42.0%n" +
                                  " but was: 42.0f"));
  }

  @Test
  void should_create_AssertionError_with_message_differentiating_expected_and_actual_persons() {
    // GIVEN
    Person actual = new Person("Jake", 43);
    Person expected = new Person("Jake", 47);
    ShouldBeEqual shouldBeEqual = (ShouldBeEqual) shouldBeEqual(actual, expected, STANDARD_REPRESENTATION);
    shouldBeEqual.descriptionFormatter = mock(DescriptionFormatter.class);
    when(shouldBeEqual.descriptionFormatter.format(description)).thenReturn(formattedDescription);
    // WHEN
    AssertionError error = shouldBeEqual.newAssertionError(description, STANDARD_REPRESENTATION);
    // THEN
    then(error).isInstanceOf(AssertionFailedError.class)
               .hasMessage("[my test] %n" +
                           "expected: \"Person[name=Jake] (Person@%s)\"%n" +
                           " but was: \"Person[name=Jake] (Person@%s)\"",
                           toHexString(expected.hashCode()), toHexString(actual.hashCode()));
  }

  @Test
  void should_create_AssertionError_with_message_differentiating_expected_and_actual_persons_even_if_a_comparator_based_comparison_strategy_is_used() {
    // GIVEN
    Person actual = new Person("Jake", 43);
    Person expected = new Person("Jake", 47);
    ComparisonStrategy ageComparisonStrategy = new ComparatorBasedComparisonStrategy(new PersonComparator());
    ShouldBeEqual shouldBeEqual = (ShouldBeEqual) shouldBeEqual(actual, expected, ageComparisonStrategy, STANDARD_REPRESENTATION);
    shouldBeEqual.descriptionFormatter = mock(DescriptionFormatter.class);
    when(shouldBeEqual.descriptionFormatter.format(description)).thenReturn(formattedDescription);
    // WHEN
    AssertionError error = shouldBeEqual.newAssertionError(description, STANDARD_REPRESENTATION);
    // THEN
    then(error).isInstanceOf(AssertionFailedError.class)
               .hasMessage("[my test] %n" +
                           "expected: \"Person[name=Jake] (Person@%s)\"%n" +
                           " but was: \"Person[name=Jake] (Person@%s)\"%n" +
                           "when comparing values using PersonComparator",
                           toHexString(expected.hashCode()), toHexString(actual.hashCode()));
  }

  @Test
  void should_create_AssertionError_with_message_differentiating_null_and_object_with_null_toString() {
    // GIVEN
    Object actual = null;
    Object expected = new ToStringIsNull();
    ShouldBeEqual shouldBeEqual = (ShouldBeEqual) shouldBeEqual(actual, expected, STANDARD_REPRESENTATION);
    shouldBeEqual.descriptionFormatter = mock(DescriptionFormatter.class);
    when(shouldBeEqual.descriptionFormatter.format(description)).thenReturn(formattedDescription);
    // WHEN
    AssertionError error = shouldBeEqual.newAssertionError(description, STANDARD_REPRESENTATION);
    // THEN
    then(error).isInstanceOf(AssertionFailedError.class)
               .hasMessage("[my test] %n" +
                           "expected: \"null (ToStringIsNull@%s)\"%n" +
                           " but was: null",
                           toHexString(expected.hashCode()));
  }

  @Test
  void should_create_AssertionError_with_message_differentiating_object_with_null_toString_and_null() {
    // GIVEN
    Object actual = new ToStringIsNull();
    Object expected = null;
    ShouldBeEqual shouldBeEqual = (ShouldBeEqual) shouldBeEqual(actual, expected, STANDARD_REPRESENTATION);
    shouldBeEqual.descriptionFormatter = mock(DescriptionFormatter.class);
    when(shouldBeEqual.descriptionFormatter.format(description)).thenReturn(formattedDescription);
    // WHEN
    AssertionError error = shouldBeEqual.newAssertionError(description, STANDARD_REPRESENTATION);
    // THEN
    then(error).isInstanceOf(AssertionFailedError.class)
               .hasMessage("[my test] %n" +
                           "expected: null%n" +
                           " but was: \"null (ToStringIsNull@%s)\"",
                           toHexString(actual.hashCode()));
  }

  private static class Person {
    private final String name;
    private final int age;

    public Person(String name, int age) {
      this.name = name;
      this.age = age;
    }

    @Override
    public String toString() {
      return concat("Person[name=", name, "]");
    }
  }

  private static class PersonComparator implements Comparator<Person> {
    @Override
    public int compare(Person p1, Person p2) {
      return p1.age - p2.age;
    }
  }

  public static class ToStringIsNull {
    @Override
    public String toString() {
      return null;
    }
  }

}
