package org.assertj.core.api.hashset;

import static java.time.Instant.EPOCH;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainOnly.shouldContainOnly;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.HashSet;

import org.assertj.core.api.HashSetAssert;
import org.assertj.core.api.HashSetAssertBaseTest;

class HashSetAssert_containsOnly_Test extends HashSetAssertBaseTest {

  @Override
  protected HashSetAssert<Object> invoke_api_method() {
    return assertions.containsOnly(someValues);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertContainsOnly(getInfo(assertions), getActual(assertions), someValues);
  }

  @HashSetFactory.Test
  void should_pass(HashSetFactory hashSetFactory) {
    // GIVEN
    HashSet<String> hashSet = hashSetFactory.createWith("Yoda", "Yoda", "Luke", "Han");
    // WHEN/THEN
    assertThatNoException().isThrownBy(() -> assertThat(hashSet).containsOnly("Yoda", "Luke", "Luke", "Han"));
  }

  @HashSetFactory.Test
  void should_fail_for_not_listed_elements_and_elements_with_changed_hashCode(HashSetFactory hashSetFactory) {
    // GIVEN
    Date first = Date.from(EPOCH.plusSeconds(1));
    Date second = Date.from(EPOCH.plusSeconds(2));
    HashSet<Date> dates = hashSetFactory.createWith(first, second);
    // WHEN
    first.setTime(3_000);
    Date asOriginalFirst = Date.from(EPOCH.plusSeconds(1));
    AssertionError assertionError = expectAssertionError(() -> assertThat(dates).containsOnly(first, asOriginalFirst));
    // THEN
    String messageStart = shouldContainOnly(dates,
                                            new Date[] { first, asOriginalFirst },
                                            asList(first, asOriginalFirst),
                                            hashSetFactory.createWith(first, second)).create();
    then(assertionError).hasMessageStartingWith(messageStart).hasMessageContaining("hashCode").hasMessageContaining("changed");
  }
}
