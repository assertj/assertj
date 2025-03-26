package org.assertj.core.api.hashset;

import static java.time.Instant.EPOCH;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.HashSet;

import org.assertj.core.api.HashSetAssert;
import org.assertj.core.api.HashSetAssertBaseTest;
import org.junit.jupiter.api.Test;

class HashSetAssert_containsSequence_Test extends HashSetAssertBaseTest {
  @Override
  protected HashSetAssert<Object> invoke_api_method() {
    return assertions.containsSequence(someValues).containsSequence(asList(someValues));
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables, times(2)).assertContainsSequence(getInfo(assertions), getActual(assertions), someValues);
  }

  @HashSetFactory.Test
  void should_pass(HashSetFactory hashSetFactory) {
    // GIVEN
    Date first = Date.from(EPOCH.plusSeconds(1));
    Date second = Date.from(EPOCH.plusSeconds(2));
    Date third = Date.from(EPOCH.plusSeconds(3));
    HashSet<Date> dates = hashSetFactory.createWith(first, second, third);
    // WHEN
    Date[] exactElements = dates.toArray(new Date[0]);
    // THEN
    assertThatNoException().isThrownBy(() -> assertThat(dates).containsSequence(exactElements[1], exactElements[2])
                                                              .containsSequence(dates));
  }

  @Test
  void should_fail_after_hashCode_changed() {
    // GIVEN
    Date first = Date.from(EPOCH.plusSeconds(1));
    Date second = Date.from(EPOCH.plusSeconds(2));
    Date third = Date.from(EPOCH.plusSeconds(3));
    Date fourth = Date.from(EPOCH.plusSeconds(4));
    HashSet<Date> dates = newLinkedHashSet(first, second, third, fourth);
    // WHEN
    third.setTime(5_000);
    AssertionError assertionError = expectAssertionError(() -> assertThat(dates).containsSequence(second, third));
    // THEN
    then(assertionError)
                        .hasMessageContaining(STANDARD_REPRESENTATION.toStringOf(third))
                        .hasMessageContaining("hashCode").hasMessageContaining("changed");
  }

  @Test
  void should_fail_after_hashCode_changed_for_Iterable() {
    // GIVEN
    Date first = Date.from(EPOCH.plusSeconds(1));
    Date second = Date.from(EPOCH.plusSeconds(2));
    Date third = Date.from(EPOCH.plusSeconds(3));
    Date fourth = Date.from(EPOCH.plusSeconds(4));
    HashSet<Date> dates = newLinkedHashSet(first, second, third, fourth);
    // WHEN
    third.setTime(5_000);
    AssertionError assertionError = expectAssertionError(() -> assertThat(dates).containsSequence(asList(second, third)));
    // THEN
    then(assertionError)
                        .hasMessageContaining(STANDARD_REPRESENTATION.toStringOf(third))
                        .hasMessageContaining("hashCode").hasMessageContaining("changed");
  }
}
