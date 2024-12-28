package org.assertj.core.api.hashset;

import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainsOnlyOnce.shouldContainsOnlyOnce;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.HashSet;

import org.assertj.core.api.HashSetAssert;
import org.assertj.core.api.HashSetAssertBaseTest;

class HashSetAssert_containsOnlyOnce_Test extends HashSetAssertBaseTest {

  @Override
  protected HashSetAssert<Object> invoke_api_method() {
    return assertions.containsOnlyOnce(someValues);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertContainsOnlyOnce(getInfo(assertions), getActual(assertions), someValues);
  }

  @HashSetFactory.Test
  void should_pass(HashSetFactory hashSetFactory) {
    // GIVEN
    HashSet<String> hashSet = hashSetFactory.createWith("Yoda", "Yoda", "Luke", "Han");
    // WHEN/THEN
    assertThatNoException().isThrownBy(() -> assertThat(hashSet).containsOnlyOnce("Yoda", "Luke", "Han"));
  }

  @HashSetFactory.Test
  void should_fail_for_missing_elements_and_elements_with_changed_hashCode(HashSetFactory hashSetFactory) {
    // GIVEN
    Date first = Date.from(EPOCH.plusSeconds(1));
    Date second = Date.from(EPOCH.plusSeconds(3));
    HashSet<Date> dates = hashSetFactory.createWith(first, second);
    // EXPECT
    assertThat(dates).containsOnly(first, second);
    assertThat(dates).containsOnlyOnce(first, second);
    // WHEN
    first.setTime(3_000);
    Date asOriginalFirst = Date.from(EPOCH.plusSeconds(1));
    AssertionError assertionError = expectAssertionError(() -> assertThat(dates).containsOnlyOnce(second, asOriginalFirst));
    // THEN
    String messageStart = shouldContainsOnlyOnce(dates,
                                                 new Date[] { second, asOriginalFirst },
                                                 newLinkedHashSet(asOriginalFirst),
                                                 newLinkedHashSet(second)).create();
    then(assertionError).hasMessageStartingWith(messageStart).hasMessageContaining("hashCode").hasMessageContaining("changed");
  }
}
