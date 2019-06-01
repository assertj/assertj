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
    List<Person> peopleList = Arrays.asList(sheldon, leonard, howard, raj);

    assertThat(peopleList).usingRecursiveComparison().contains(raj, leonard, sheldon);
  }

  @Test
  public void should_fail_when_iterable_does_not_contain_all_items() {
    List<Person> peopleList = Arrays.asList(sheldon, leonard, howard, raj);

    expectAssertionError(() -> {
      assertThat(peopleList).usingRecursiveComparison()
                            .contains(raj, leonard, sheldon, penny);
    });
  }

  @Test
  public void should_pass_when_items_are_not_compatible() {
    List<Person> peopleList = Arrays.asList(sheldon, leonard, howard, raj);
    PersonDto sheldonDto = new PersonDto("Sheldon");
    PersonDto leonardDto = new PersonDto("Leonard");

    assertThat(peopleList).usingRecursiveComparison().contains(sheldonDto, leonardDto);
  }

  @Test
  public void should_fail_when_items_are_not_compatible_with_strict_type_check() {
    List<Person> peopleList = Arrays.asList(sheldon, leonard, howard, raj);
    PersonDto sheldonDto = new PersonDto("Sheldon");
    PersonDto leonardDto = new PersonDto("Leonard");

    expectAssertionError(() -> {
      assertThat(peopleList).usingRecursiveComparison()
        .withStrictTypeChecking()
        .contains(sheldonDto, leonardDto);
    });
  }

}
