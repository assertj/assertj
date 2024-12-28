package org.assertj.core.api.hashset;

import static java.time.Instant.EPOCH;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainExactlyInAnyOrder.shouldContainExactlyInAnyOrder;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.HashSet;

import org.assertj.core.api.HashSetAssert;
import org.assertj.core.api.HashSetAssertBaseTest;
import org.assertj.core.internal.StandardComparisonStrategy;

class HashSetAssert_containsExactlyInAnyOrder_Test extends HashSetAssertBaseTest {

  @Override
  protected HashSetAssert<Object> invoke_api_method() {
    return assertions.containsExactlyInAnyOrder(someValues);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertContainsExactlyInAnyOrder(getInfo(assertions), getActual(assertions), someValues);
  }

  @HashSetFactory.Test
  void should_pass(HashSetFactory hashSetFactory) {
    // GIVEN
    HashSet<String> hashSet = hashSetFactory.createWith("Yoda", "Yoda", "Luke", "Han");
    // WHEN/THEN
    assertThatNoException().isThrownBy(() -> assertThat(hashSet).containsExactlyInAnyOrder("Yoda", "Han", "Luke"));
  }

  @HashSetFactory.Test
  void should_fail_for_missing_elements_and_elements_with_changed_hashCode(HashSetFactory hashSetFactory) {
    // GIVEN
    Date first = Date.from(EPOCH.plusSeconds(1));
    Date second = Date.from(EPOCH.plusSeconds(2));
    HashSet<Date> dates = hashSetFactory.createWith(first, second);
    // WHEN
    first.setTime(3_000);
    Date asOriginalFirst = Date.from(EPOCH.plusSeconds(1));
    Date missing = Date.from(EPOCH.plusSeconds(4));
    AssertionError assertionError = expectAssertionError(() -> assertThat(dates).containsExactlyInAnyOrder(first, asOriginalFirst,
                                                                                                           second, missing));
    // THEN
    String messageStart = shouldContainExactlyInAnyOrder(dates,
                                                         new Date[] { first, asOriginalFirst, second, missing },
                                                         asList(first, asOriginalFirst, missing),
                                                         singletonList(first),
                                                         StandardComparisonStrategy.instance()).create();
    then(assertionError).hasMessageStartingWith(messageStart).hasMessageContaining("hashCode").hasMessageContaining("changed");
  }
}
