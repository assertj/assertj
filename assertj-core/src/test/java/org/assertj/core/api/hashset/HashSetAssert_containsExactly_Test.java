package org.assertj.core.api.hashset;

import static java.time.Instant.EPOCH;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactly;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.HashSet;

import org.assertj.core.api.HashSetAssert;
import org.assertj.core.api.HashSetAssertBaseTest;

class HashSetAssert_containsExactly_Test extends HashSetAssertBaseTest {

  @Override
  protected HashSetAssert<Object> invoke_api_method() {
    return assertions.containsExactly(someValues);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertContainsExactly(getInfo(assertions), getActual(assertions), someValues);
  }

  @HashSetFactory.Test
  void should_pass(HashSetFactory hashSetFactory) {
    // GIVEN
    HashSet<String> hashSet = hashSetFactory.createWith("Yoda", "Yoda", "Luke", "Han");
    // WHEN
    String[] exactElements = hashSetFactory.createWith("Yoda", "Luke", "Han").toArray(new String[0]);
    // THEN
    assertThatNoException().isThrownBy(() -> assertThat(hashSet).containsExactly(exactElements));
  }

  @HashSetFactory.Test
  void should_fail_finding_elements_with_changed_hashCode(HashSetFactory hashSetFactory) {
    // GIVEN
    Date first = Date.from(EPOCH.plusSeconds(1));
    Date second = Date.from(EPOCH.plusSeconds(2));
    HashSet<Date> dates = hashSetFactory.createWith(first, second);
    // WHEN
    first.setTime(3_000);
    AssertionError assertionError = expectAssertionError(() -> assertThat(dates).containsExactly(first, second));
    // THEN
    String messageStart = shouldContainExactly(dates, asList(first, second), singletonList(first), singletonList(first)).create();
    then(assertionError).hasMessageStartingWith(messageStart).hasMessageContaining("hashCode").hasMessageContaining("changed");
  }
}
