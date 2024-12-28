package org.assertj.core.api.hashset;

import static java.time.Instant.EPOCH;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.ShouldNotContainSubsequence.shouldNotContainSubsequence;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.IterableUtil.toArray;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.HashSet;

import org.assertj.core.api.HashSetAssert;
import org.assertj.core.api.HashSetAssertBaseTest;
import org.junit.jupiter.api.Test;

class HashSetAssert_doesNotContainSubsequence_Test extends HashSetAssertBaseTest {
  @Override
  protected HashSetAssert<Object> invoke_api_method() {
    return assertions.doesNotContainSubsequence(someValues).doesNotContainSubsequence(asList(someValues));
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables, times(2)).assertDoesNotContainSubsequence(getInfo(assertions), getActual(assertions), someValues);
  }

  @HashSetFactory.Test
  void should_pass(HashSetFactory hashSetFactory) {
    // GIVEN
    Date first = Date.from(EPOCH.plusSeconds(1));
    Date second = Date.from(EPOCH.plusSeconds(2));
    Date third = Date.from(EPOCH.plusSeconds(3));
    HashSet<Date> dates = hashSetFactory.createWith(first, second, third);
    // WHEN
    Date[] exactElements = hashSetFactory.createWith(third, first).toArray(new Date[0]);
    // THEN
    assertThatNoException().isThrownBy(() -> assertThat(dates).doesNotContainSubsequence(exactElements)
                                                              .doesNotContainSubsequence(asList(exactElements)));
  }

  @HashSetFactory.Test
  void should_fail_in_ordinary_scenario(HashSetFactory hashSetFactory) {
    // GIVEN
    HashSet<String> toTest = hashSetFactory.createWith("Yoda", "Luke");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(toTest).doesNotContainSubsequence(toTest));
    // THEN
    then(assertionError).hasMessageStartingWith(shouldNotContainSubsequence(toTest, toArray(toTest), 0).create());
  }

  @Test
  void should_pass_after_hashCode_changed() {
    // GIVEN
    Date first = Date.from(EPOCH.plusSeconds(1));
    Date second = Date.from(EPOCH.plusSeconds(2));
    Date third = Date.from(EPOCH.plusSeconds(3));
    HashSet<Date> dates = newLinkedHashSet(first, second, third);
    // WHEN
    first.setTime(4_000);
    // THEN
    assertThatNoException().isThrownBy(() -> assertThat(dates).doesNotContainSubsequence(first, third));
  }

  @Test
  void should_fail_for_null() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat((HashSet<String>) null).doesNotContainSubsequence(""));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }
}
