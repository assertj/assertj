package org.assertj.core.api.hashset;

import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainAnyOf.shouldContainAnyOf;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.HashSet;

import org.assertj.core.api.HashSetAssert;
import org.assertj.core.api.HashSetAssertBaseTest;

class HashSetAssert_containsAnyOf_Test extends HashSetAssertBaseTest {
  @Override
  protected HashSetAssert<Object> invoke_api_method() {
    return assertions.containsAnyOf(someValues);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertContainsAnyOf(getInfo(assertions), getActual(assertions), someValues);
  }

  @HashSetFactory.Test
  void should_pass(HashSetFactory hashSetFactory) {
    // GIVEN
    HashSet<String> hasSet = hashSetFactory.createWith("Yoda", "Luke", "Han");
    // WHEN/THEN
    assertThatNoException().isThrownBy(() -> assertThat(hasSet).containsAnyOf("Han", "Qui-Gon"));
  }

  @HashSetFactory.Test
  void should_fail_for_elements_with_changed_hashCode(HashSetFactory hashSetFactory) {
    // GIVEN
    Date first = Date.from(EPOCH.plusSeconds(1));
    Date second = Date.from(EPOCH.plusSeconds(2));
    HashSet<Date> dates = hashSetFactory.createWith(first, second);
    // WHEN
    first.setTime(3_000);
    Date asOriginalFirst = Date.from(EPOCH.plusSeconds(1));
    AssertionError assertionError = expectAssertionError(() -> assertThat(dates).containsAnyOf(first, asOriginalFirst));
    // THEN
    String messageStart = shouldContainAnyOf(dates, new Date[] { first, asOriginalFirst }).create();
    then(assertionError).hasMessageStartingWith(messageStart).hasMessageContaining("hashCode").hasMessageContaining("changed");
  }
}
