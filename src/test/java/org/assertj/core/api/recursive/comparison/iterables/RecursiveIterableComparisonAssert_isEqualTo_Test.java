package org.assertj.core.api.recursive.comparison.iterables;

import org.assertj.core.api.RecursiveIterableComparisonAssert_isEqualTo_BaseTest;
import org.assertj.core.internal.objects.data.Person;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.list;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;

public class RecursiveIterableComparisonAssert_isEqualTo_Test extends RecursiveIterableComparisonAssert_isEqualTo_BaseTest {

  @Test
  public void should_pass_when_iterables_are_null() {
    // GIVEN
    List<Person> actual = null;
    List<Person> expected = null;
    //THEN
    assertThat(actual).usingRecursiveComparison()
      .isEqualTo(expected);
  }

  @Test
  public void should_fail_when_actual_is_null_and_expected_is_not() {
    // GIVEN
    List<Person> actual = null;
    List<Person> expected = list(sheldon);
    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);
    // THEN
    verify(failures).failure(info, shouldNotBeNull());
  }

  @Test
  public void should_fail_when_actual_is_not_null_and_expected_is() {
    // GIVEN
    List<Person> actual = list(sheldon);
    List<Person> expected = null;
    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);
    // THEN
    verify(failures).failure(info, shouldBeEqual(actual, expected, objects.getComparisonStrategy(), info.representation()));
  }

  @Test
  public void should_pass_when_iterables_share_same_items_in_same_order() {
    // GIVEN
    List<Person> actual = list(sheldon, leonard, howard, raj);
    Set<Person> expected = new LinkedHashSet<>(actual);
    // THEN
    assertThat(actual).usingRecursiveComparison()
      .isEqualTo(expected);
  }

  @Test
  public void should_fail_when_iterables_contain_different_items() {
    // GIVEN
    List<Person> actual = Arrays.asList(sheldon, raj);
    List<Person> expected = Arrays.asList(sheldon, penny);
    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);
    // THEN
    verify(failures).failure(info, shouldBeEqual(actual, expected, objects.getComparisonStrategy(), info.representation()));
  }

  @Test
  public void should_fail_when_iterables_share_same_items_but_in_different_order() {
    // GIVEN
    List<Person> actual = list(sheldon, leonard);
    List<Person> expected = list(leonard, sheldon);
    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);
    // THEN
    verify(failures).failure(info, shouldBeEqual(actual, expected, objects.getComparisonStrategy(), info.representation()));
  }

  @Test
  public void should_fail_when_iterable_sizes_are_different() {
    // GIVEN
    List<Person> actual = list(sheldon, leonard, howard, raj);
    List<Person> expected = list(sheldon, leonard, howard);
    // THEN
    compareRecursivelyFailsAsExpected(actual, expected);
  }

  @Test
  public void should_fail_when_comparing_iterable_to_array_by_default() {
    // GIVEN
    List<Person> actual = Arrays.asList(sheldon, leonard, howard, raj);
    Person[] expected = {sheldon, leonard, howard, raj};
    // THEN
    expectAssertionError(() -> {
      assertThat(actual).usingRecursiveComparison()
        .isEqualTo(expected);
    });
  }

  @Test
  public void should_pass_when_iterable_is_compared_to_array_with_allowComparingIterableWithArray() {
    // GIVEN
    List<Person> actual = Arrays.asList(sheldon, leonard, howard, raj);
    Person[] expected = {sheldon, leonard, howard, raj};
    // THEN
    assertThat(actual).usingRecursiveComparison()
      .allowingComparingIterableWithArray()
      .isEqualTo(expected);
  }
}
