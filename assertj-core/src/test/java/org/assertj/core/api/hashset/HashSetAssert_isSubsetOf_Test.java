package org.assertj.core.api.hashset;

import static java.time.Instant.EPOCH;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeSubsetOf.shouldBeSubsetOf;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.HashSet;

import org.assertj.core.api.HashSetAssert;
import org.assertj.core.api.HashSetAssertBaseTest;

class HashSetAssert_isSubsetOf_Test extends HashSetAssertBaseTest {

  @Override
  protected HashSetAssert<Object> invoke_api_method() {
    return assertions.isSubsetOf(asList(someValues)).isSubsetOf(someValues);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables, never()).assertIsSubsetOf(getInfo(assertions), getActual(assertions), asList(someValues));
  }

  @HashSetFactory.Test
  void should_pass(HashSetFactory hashSetFactory) {
    // GIVEN
    HashSet<String> hashSet = hashSetFactory.createWith("Yoda", "Luke", "Yoda", "Han");
    // WHEN/THEN
    assertThatNoException().isThrownBy(() -> assertThat(hashSet)
                                                                .isSubsetOf("Obi", "Qui-Gon", "Yoda", "Luke", "Han")
                                                                .isSubsetOf(asList("Qui-Gon", "Luke", "Han", "Yoda")));
  }

  @HashSetFactory.Test
  void should_fail_when_hashCode_changed_as_in_superset(HashSetFactory hashSetFactory) {
    // GIVEN
    Date first = Date.from(EPOCH.plusSeconds(1));
    Date second = Date.from(EPOCH.plusSeconds(2));
    HashSet<Date> dates = hashSetFactory.createWith(first, second);
    // WHEN
    first.setTime(3_000);
    // THEN
    assertThatNoException().isThrownBy(() -> assertThat(dates).isSubsetOf(second, Date.from(EPOCH.plusSeconds(3))));
  }

  @HashSetFactory.Test
  void should_fail_when_hashCode_changed_differently_than_in_superset(HashSetFactory hashSetFactory) {
    // GIVEN
    Date first = Date.from(EPOCH.plusSeconds(1));
    Date second = Date.from(EPOCH.plusSeconds(2));
    HashSet<Date> dates = hashSetFactory.createWith(first, second);
    // WHEN
    first.setTime(3_000);
    Date asOriginalFirst = Date.from(EPOCH.plusSeconds(1));
    AssertionError assertionError = expectAssertionError(() -> assertThat(dates).isSubsetOf(asList(asOriginalFirst, second)));
    // THEN
    String messageStart = shouldBeSubsetOf(dates, asList(asOriginalFirst, second), singletonList(first)).create();
    then(assertionError).hasMessageStartingWith(messageStart).hasMessageNotContaining("hashCode");
  }
}
