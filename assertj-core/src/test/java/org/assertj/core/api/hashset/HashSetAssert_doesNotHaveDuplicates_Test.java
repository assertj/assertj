package org.assertj.core.api.hashset;

import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotHaveDuplicates.shouldNotHaveDuplicates;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.HashSet;

import org.assertj.core.api.HashSetAssert;
import org.assertj.core.api.HashSetAssertBaseTest;

class HashSetAssert_doesNotHaveDuplicates_Test extends HashSetAssertBaseTest {

  @Override
  protected HashSetAssert<Object> invoke_api_method() {
    return assertions.doesNotHaveDuplicates();
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertDoesNotHaveDuplicates(getInfo(assertions), getActual(assertions));
  }

  @HashSetFactory.Test
  void should_pass(HashSetFactory hashSetFactory) {
    // GIVEN
    HashSet<String> hashSet = hashSetFactory.createWith("Yoda", "Yoda", "Luke", "Han");
    // WHEN/THEN
    assertThatNoException().isThrownBy(() -> assertThat(hashSet).doesNotHaveDuplicates());
  }

  @HashSetFactory.Test
  void should_fail_for_elements_with_changed_hashCode(HashSetFactory hashSetFactory) {
    // GIVEN
    Date first = Date.from(EPOCH.plusSeconds(1));
    Date second = Date.from(EPOCH.plusSeconds(3));
    HashSet<Date> dates = hashSetFactory.createWith(first, second);
    // WHEN
    first.setTime(3_000);
    AssertionError assertionError = expectAssertionError(() -> assertThat(dates).doesNotHaveDuplicates());
    // THEN
    String messageStart = shouldNotHaveDuplicates(dates, newLinkedHashSet(first)).create();
    then(assertionError).hasMessageStartingWith(messageStart).hasMessageContaining("hashCode").hasMessageContaining("changed");
  }
}
