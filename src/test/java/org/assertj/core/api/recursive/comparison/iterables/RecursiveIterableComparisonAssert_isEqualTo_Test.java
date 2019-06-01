package org.assertj.core.api.recursive.comparison.iterables;

import org.assertj.core.internal.objects.data.Person;
import org.assertj.core.internal.objects.data.PersonDto;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

public class RecursiveIterableComparisonAssert_isEqualTo_Test {

  private Person sheldon = new Person("Sheldon");
  private Person leonard = new Person("Leonard");
  private Person howard = new Person("Howard");
  private Person raj = new Person("Rajesh");
  private Person penny = new Person("Penny");

  @Test
  public void should_pass_when_iterables_share_same_items_in_same_order() {
    List<Person> peopleList = Arrays.asList(sheldon, leonard, howard, raj);
    Set<Person> peopleSet = new LinkedHashSet<>(peopleList);

    assertThat(peopleList).usingRecursiveComparison()
      .isEqualTo(peopleSet);
  }

  @Test
  public void should_fail_when_items_are_different() {
    List<Person> peopleList = Arrays.asList(sheldon, leonard, howard, raj);
    List<Person> peopleList2 = Arrays.asList(sheldon, leonard, howard, penny);

    expectAssertionError(() -> {
      assertThat(peopleList).usingRecursiveComparison()
        .isEqualTo(peopleList2);
    });
  }

  @Test
  public void should_fail_when_sizes_are_different() {
    List<Person> peopleList = Arrays.asList(sheldon, leonard, howard, raj);
    List<Person> peopleList2 = Arrays.asList(sheldon, leonard, howard);

    expectAssertionError(() -> {
      assertThat(peopleList).usingRecursiveComparison()
        .isEqualTo(peopleList2);
    });
  }

  @Test
  public void should_fail_when_expected_is_array() {
    List<Person> peopleList = Arrays.asList(sheldon, leonard, howard, raj);
    Person[] peopleArray = {sheldon, leonard, howard, raj};

    expectAssertionError(() -> {
      assertThat(peopleList).usingRecursiveComparison()
        .isEqualTo(peopleArray);
    });
  }

  @Test
  public void should_pass_when_expected_is_allowed_to_be_array() {
    List<Person> peopleList = Arrays.asList(sheldon, leonard, howard, raj);
    Person[] peopleArray = {sheldon, leonard, howard, raj};

    assertThat(peopleList).usingRecursiveComparison()
      .allowingArrayTypeForExpected()
      .isEqualTo(peopleArray);
  }

  @Test
  public void should_fail_when_iterables_do_not_share_order() {
    List<Person> peopleList = Arrays.asList(sheldon, leonard, howard, raj);
    Set<Person> peopleSet = new HashSet<>(peopleList);

    expectAssertionError(() -> {
      assertThat(peopleList).usingRecursiveComparison()
        .isEqualTo(peopleSet);
    });
  }

  @Test
  public void should_pass_when_actual_iterable_order_is_ignored() {
    List<Person> peopleList = Arrays.asList(sheldon, leonard, howard, raj);
    Set<Person> peopleSet = new HashSet<>(peopleList);

    assertThat(peopleList).usingRecursiveComparison()
      .ignoringActualIterableOrder()
      .isEqualTo(peopleSet);
  }

  @Test
  public void should_fail_when_iterables_themselves_are_not_compatible_with_strict_type_check_on_actual_iterable() {
    List<Person> peopleList = Arrays.asList(sheldon, leonard, howard, raj);
    Set<Person> peopleSet = new LinkedHashSet<>(peopleList);

    expectAssertionError(() -> {
      assertThat(peopleList).usingRecursiveComparison()
        .withStrictTypeCheckingOnActualIterable()
        .isEqualTo(peopleSet);
    });
  }

  @Test
  public void should_pass_when_elements_contained_in_iterables_are_not_compatible() {
    PersonDto sheldonDto = new PersonDto("Sheldon");
    PersonDto leonardDto = new PersonDto("Leonard");
    PersonDto howardDto = new PersonDto("Howard");
    PersonDto rajDto = new PersonDto("Rajesh");

    List<Person> peopleList = Arrays.asList(sheldon, leonard, howard, raj);
    List<PersonDto> peopleDtoList = Arrays.asList(sheldonDto, leonardDto, howardDto, rajDto);

    assertThat(peopleList).usingRecursiveComparison().isEqualTo(peopleDtoList);
  }

  @Test
  public void should_fail_when_elements_contained_in_iterables_are_not_compatible_with_strict_type_check() {
    PersonDto sheldonDto = new PersonDto("Sheldon");
    PersonDto leonardDto = new PersonDto("Leonard");
    PersonDto howardDto = new PersonDto("Howard");
    PersonDto rajDto = new PersonDto("Rajesh");

    List<Person> peopleList = Arrays.asList(sheldon, leonard, howard, raj);
    List<PersonDto> peopleDtoList = Arrays.asList(sheldonDto, leonardDto, howardDto, rajDto);

    expectAssertionError(() -> {
      assertThat(peopleList).usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(peopleDtoList);
    });
  }

}
