/*
 * Created on Aug 6, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.error;

import static java.lang.Integer.toHexString;

import static org.fest.assertions.error.ShouldBeEqual.shouldBeEqual;
import static org.fest.util.Strings.concat;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Comparator;

import org.junit.Before;
import org.junit.Test;

import org.fest.assertions.description.Description;
import org.fest.assertions.internal.*;

/**
 * Tests for <code>{@link ShouldBeEqual#newAssertionError(Description)}</code>.
 * 
 * @author Joel Costigliola (based on Tomasz Nurkiewicz ideas)
 */
public class ShouldBeEqual_newAssertionError_differentiating_expected_and_actual_Test {

  private String formattedDescription = "[my test]";

  private Description description;
  private ShouldBeEqual shouldBeEqual;

  @Before
  public void setUp() {
    description = new TestDescription("my test");
  }

  @Test
  public void should_create_AssertionError_with_message_differentiating_expected_double_and_actual_float() {
    Float actual = 42f;
    Double expected = 42d;
    shouldBeEqual = (ShouldBeEqual) shouldBeEqual(actual, expected);
    shouldBeEqual.descriptionFormatter = mock(DescriptionFormatter.class);
    when(shouldBeEqual.descriptionFormatter.format(description)).thenReturn(formattedDescription);
    AssertionError error = shouldBeEqual.newAssertionError(description);
    assertEquals("[my test] expected:<42.0[]> but was:<42.0[f]>", error.getMessage());
  }

  @Test
  public void should_create_AssertionError_with_message_differentiating_expected_and_actual_persons() {
    Person actual = new Person("Jake", 43);
    Person expected = new Person("Jake", 47);
    shouldBeEqual = (ShouldBeEqual) shouldBeEqual(actual, expected);
    shouldBeEqual.descriptionFormatter = mock(DescriptionFormatter.class);
    when(shouldBeEqual.descriptionFormatter.format(description)).thenReturn(formattedDescription);
    AssertionError error = shouldBeEqual.newAssertionError(description);
    assertEquals("[my test] expected:\n<'Person[name=Jake] (Person@" + toHexString(expected.hashCode())
        + ")'>\n but was:\n<'Person[name=Jake] (Person@" + toHexString(actual.hashCode()) + ")'>", error.getMessage());
  }

  @Test
  public void should_create_AssertionError_with_message_differentiating_expected_and_actual_persons_even_if_a_comparator_based_comparison_strategy_is_used() {
    Person actual = new Person("Jake", 43);
    Person expected = new Person("Jake", 47);
    ComparisonStrategy ageComparisonStrategy = new ComparatorBasedComparisonStrategy(new PersonComparator());
    shouldBeEqual = (ShouldBeEqual) shouldBeEqual(actual, expected, ageComparisonStrategy);
    shouldBeEqual.descriptionFormatter = mock(DescriptionFormatter.class);
    when(shouldBeEqual.descriptionFormatter.format(description)).thenReturn(formattedDescription);
    AssertionError error = shouldBeEqual.newAssertionError(description);
    assertEquals("[my test] Expecting actual:\n<'Person[name=Jake] (Person@" + toHexString(actual.hashCode())
        + ")'>\n to be equal to \n<'Person[name=Jake] (Person@" + toHexString(expected.hashCode())
        + ")'>\n according to 'PersonComparator' comparator but was not.", error.getMessage());
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
    public int compare(Person p1, Person p2) {
      return p1.age - p2.age;
    }
  }

}
