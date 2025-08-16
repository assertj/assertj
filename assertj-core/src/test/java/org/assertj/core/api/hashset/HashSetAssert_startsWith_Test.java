package org.assertj.core.api.hashset;

import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.HashSet;

import org.assertj.core.api.HashSetAssert;
import org.assertj.core.api.HashSetAssertBaseTest;
import org.junit.jupiter.api.Test;

class HashSetAssert_startsWith_Test extends HashSetAssertBaseTest {
  @Override
  protected HashSetAssert<Object> invoke_api_method() {
    return assertions.startsWith(someValues);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertStartsWith(getInfo(assertions), getActual(assertions), someValues);
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
    assertThatNoException().isThrownBy(() -> assertThat(dates).startsWith(exactElements));
  }

  @Test
  void should_fail_after_hashCode_changed() {
    // GIVEN
    Date first = Date.from(EPOCH.plusSeconds(1));
    Date second = Date.from(EPOCH.plusSeconds(2));
    Date third = Date.from(EPOCH.plusSeconds(3));
    HashSet<Date> dates = newLinkedHashSet(first, second, third);
    // WHEN
    first.setTime(4_000);
    second.setTime(5_000);
    AssertionError assertionError = expectAssertionError(() -> assertThat(dates).startsWith(first, second));
    // THEN
    then(assertionError)
                        .hasMessageContaining(STANDARD_REPRESENTATION.toStringOf(first))
                        .hasMessageContaining(STANDARD_REPRESENTATION.toStringOf(second))
                        .hasMessageContaining("hashCode").hasMessageContaining("changed");
  }
}
