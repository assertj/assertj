package org.assertj.core.api.recursive.comparison.iterables;

import org.assertj.core.internal.objects.data.Person;
import org.assertj.core.internal.objects.data.PersonDto;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

public class RecursiveIterableComparisonAssert_contains_Test {

  private Person sheldon = new Person("Sheldon");
  private Person leonard = new Person("Leonard");
  private Person howard = new Person("Howard");
  private Person raj = new Person("Rajesh");
  private Person penny = new Person("Penny");

  @Test
  public void should_pass_when_iterable_contains_items_in_any_order() {
    // GIVEN
    List<Person> actual = Arrays.asList(sheldon, leonard, howard, raj);
    // THEN
    assertThat(actual).usingRecursiveComparison()
      .contains(raj, leonard, sheldon);
  }

  @Test
  public void should_fail_when_iterable_does_not_contain_all_items() {
    // GIVEN
    List<Person> actual = Arrays.asList(sheldon, leonard, howard, raj);
    // THEN
    expectAssertionError(() -> {
      assertThat(actual).usingRecursiveComparison()
        .contains(raj, leonard, sheldon, penny);
    });
  }

  @Test
  public void should_pass_by_default_when_items_are_not_type_compatible() {
    List<Person> actual = Arrays.asList(sheldon, leonard, howard, raj);
    PersonDto sheldonDto = new PersonDto("Sheldon");
    PersonDto leonardDto = new PersonDto("Leonard");

    assertThat(actual).usingRecursiveComparison()
      .contains(sheldonDto, leonardDto);
  }

  @Test
  public void should_fail_when_items_are_not_compatible_with_strictTypeCheck() {
    // GIVEN
    List<Person> actual = Arrays.asList(sheldon, leonard, howard, raj);
    PersonDto sheldonDto = new PersonDto("Sheldon");
    PersonDto leonardDto = new PersonDto("Leonard");
    // THEN
    expectAssertionError(() -> {
      assertThat(actual).usingRecursiveComparison()
        .withStrictTypeChecking()
        .contains(sheldonDto, leonardDto);
    });
  }

}
