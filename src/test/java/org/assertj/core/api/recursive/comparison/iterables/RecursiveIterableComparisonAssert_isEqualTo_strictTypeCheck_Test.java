package org.assertj.core.api.recursive.comparison.iterables;

import org.assertj.core.api.RecursiveIterableComparisonAssert_isEqualTo_BaseTest;
import org.assertj.core.internal.objects.data.Person;
import org.assertj.core.internal.objects.data.PersonDto;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.util.Lists.list;
import static org.mockito.Mockito.verify;

public class RecursiveIterableComparisonAssert_isEqualTo_strictTypeCheck_Test extends RecursiveIterableComparisonAssert_isEqualTo_BaseTest {

  @Test
  public void should_fail_when_top_level_iterables_are_not_type_compatible_with_strictTypeChecking() {
    // GIVEN
    List<Person> actual = list(sheldon, leonard, howard, raj);
    Set<Person> expected = new LinkedHashSet<>(actual);
    recursiveComparisonConfiguration.strictTypeChecking(true);
    // THEN
    compareRecursivelyFailsAsExpected(actual, expected);
  }

  @Test
  public void should_fail_when_type_parameters_of_iterables_are_not_compatible_with_strictTypeChecking() {
    // GIVEN
    List<Person> actual = list(sheldon, leonard, howard, raj);
    List<PersonDto> expected = list(sheldonDto, leonardDto, howardDto, rajDto);
    recursiveComparisonConfiguration.strictTypeChecking(true);
    // THEN
    compareRecursivelyFailsAsExpected(actual, expected);
  }

  @Test
  public void should_pass_only_when_both_top_level_iterables_and_type_parameters_of_iterables_are_compatible_with_strictTypeChecking() {
    // GIVEN
    List<Person> actual = new LinkedList<>(list(sheldon, leonard, howard, raj));
    List<Person> expected = new LinkedList<>(list(sheldon, leonard, howard, raj));
    // THEN
    assertThat(actual).usingRecursiveComparison()
      .withStrictTypeChecking()
      .isEqualTo(expected);
  }
}
