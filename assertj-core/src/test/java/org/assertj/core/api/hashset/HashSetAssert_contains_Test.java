package org.assertj.core.api.hashset;

import org.assertj.core.api.HashSetAssertBaseTest;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Sets.newHashSet;

class HashSetAssert_contains_Test extends HashSetAssertBaseTest {
  @Test
  void should_pass() {
    // WHEN/THEN
    assertThatNoException()
      .isThrownBy(() -> assertThat(newHashSet(Set.of("Yoda", "Luke", "Han"))).contains("Han", "Luke"));
  }

  @Test
  void should_fail_if_missing_but_also_if_hashCode_changed() {
    // GIVEN
    Date first = Date.from(EPOCH.plusSeconds(1));
    Date second = Date.from(EPOCH.plusSeconds(2));
    HashSet<Date> dates = newHashSet(Set.of(first, second));
    // WHEN
    first.setTime(3_000);
    Date missing = Date.from(EPOCH.plusSeconds(4));
    AssertionError assertionError = expectAssertionError(() -> assertThat(dates).contains(first, missing));
    // THEN
    then(assertionError).hasMessage(shouldContain(dates, new Date[] {first, missing}, Set.of(missing)).create());
    // WHEN
    assertionError = expectAssertionError(() -> assertThat(dates).contains(first, second));
    // THEN
    then(assertionError).hasMessageContaining(shouldContain(dates, new Date[] {first, second}, Set.of(first)).create())
                        .hasMessageContaining("hashCode").hasMessageContaining("changed");
  }
}
