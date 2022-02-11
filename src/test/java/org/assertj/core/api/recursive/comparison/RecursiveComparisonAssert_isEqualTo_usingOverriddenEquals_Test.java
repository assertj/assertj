package org.assertj.core.api.recursive.comparison;

import java.util.stream.Stream;

import org.assertj.core.api.RecursiveComparisonAssert_isEqualTo_BaseTest;
import org.assertj.core.internal.objects.data.AlwaysDifferentPerson;
import org.assertj.core.internal.objects.data.AlwaysEqualPerson;
import org.assertj.core.internal.objects.data.Person;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class RecursiveComparisonAssert_isEqualTo_usingOverriddenEquals_Test
    extends RecursiveComparisonAssert_isEqualTo_BaseTest implements PersonData {

  @SuppressWarnings("unused")
  @ParameterizedTest(name = "{2}: actual={0} / expected={1}")
  @MethodSource("overridden_equals_method_returns_false_data")
  void should_fail_when_overridden_equals_method_returns_false(Object actual,
                                                               Object expected,
                                                               String testDescription) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).usingRecursiveComparison()
                                                                                 .usingOverriddenEquals()
                                                                                 .isEqualTo(expected));
    // THEN
    then(assertionError).hasMessageContaining("- overridden equals methods were used in the comparison");
  }

  private static Stream<Arguments> overridden_equals_method_returns_false_data() {
    Person person1 = new AlwaysDifferentPerson();
    person1.neighbour = new Person("John");
    Person person2 = new AlwaysDifferentPerson();
    person2.neighbour = new Person("John");

    Person person3 = new Person();
    person3.neighbour = new AlwaysDifferentPerson();
    person3.neighbour.name = "John";
    Person person4 = new Person();
    person4.neighbour = new AlwaysDifferentPerson();
    person4.neighbour.name = "John";

    return Stream.of(arguments(person1, person2, "root Person is AlwaysDifferentPerson"),
                     arguments(person3, person4, "neighbour Person is AlwaysDifferentPerson"));
  }

  @SuppressWarnings("unused")
  @ParameterizedTest(name = "{2}: actual={0} / expected={1}")
  @MethodSource("overridden_equals_method_returns_true_data")
  void should_pass_when_overridden_equals_method_returns_true(Object actual,
                                                              Object expected,
                                                              String testDescription) {
    assertThat(actual).usingRecursiveComparison()
                      .usingOverriddenEquals()
                      .isEqualTo(expected);
  }

  private static Stream<Arguments> overridden_equals_method_returns_true_data() {
    Person person1 = new AlwaysEqualPerson();
    person1.neighbour = new Person("John");
    Person person2 = new AlwaysEqualPerson();
    person2.neighbour = new Person("Jack");

    Person person3 = new Person();
    person3.neighbour = new AlwaysEqualPerson();
    person3.neighbour.name = "John";
    Person person4 = new Person();
    person4.neighbour = new AlwaysEqualPerson();
    person4.neighbour.name = "Jack";

    return Stream.of(arguments(person1, person2, "root Person is AlwaysEqualPerson"),
                     arguments(person3, person4, "neighbour Person is AlwaysEqualPerson"));
  }
}
