package org.assertj.core.api.hashset;

import static java.time.Instant.EPOCH;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.HashSet;

import org.assertj.core.api.HashSetAssert;
import org.assertj.core.api.HashSetAssertBaseTest;

class HashSetAssert_contains_Test extends HashSetAssertBaseTest {
  @Override
  protected HashSetAssert<Object> invoke_api_method() {
    return assertions.contains(someValues);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertContains(getInfo(assertions), getActual(assertions), someValues);
  }

  @HashSetFactory.Test
  void should_pass(HashSetFactory hashSetFactory) {
    // GIVEN
    HashSet<String> hasSet = hashSetFactory.createWith("Yoda", "Luke", "Han");
    // WHEN/THEN
    assertThatNoException().isThrownBy(() -> assertThat(hasSet).contains("Han", "Luke"));
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
    AssertionError assertionError = expectAssertionError(() -> assertThat(dates).contains(first, asOriginalFirst, missing));
    // THEN
    String messageStart = shouldContain(dates, new Date[] { first, asOriginalFirst, missing },
                                        asList(first, asOriginalFirst, missing)).create();
    then(assertionError).hasMessageStartingWith(messageStart).hasMessageContaining("hashCode").hasMessageContaining("changed");
  }

  @HashSetFactory.Test
  void should_pass_if_hashCode_changed_but_its_comparison_skipped(HashSetFactory hashSetFactory) {
    // GIVEN
    Date first = Date.from(EPOCH.plusSeconds(1));
    Date second = Date.from(EPOCH.plusSeconds(2));
    HashSet<Date> dates = hashSetFactory.createWith(first, second);
    // WHEN
    first.setTime(3_000);
    // THEN
    assertThatNoException().isThrownBy(() -> assertThat(dates).skippingHashCodeComparison().contains(first, second));
  }
}
