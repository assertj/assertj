package org.assertj.core.internal.iterators;

import org.assertj.core.internal.IterablesBaseTest;
import org.assertj.core.internal.IteratorsBaseTest;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.error.ShouldBeExhausted.shouldBeExhausted;
import static org.assertj.core.error.ShouldHaveNext.shouldHaveNext;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

public class Iterators_assertIsExhausted_Test extends IteratorsBaseTest {

  @Test
  void should_pass_if_iterator_is_exhausted() {
    // GIVEN
    List<String> list = List.of("Luke");
    Iterator iterator = list.iterator();
    // WHEN
    iterator.next();
    // THEN
    iterators.assertIsExhausted(info, iterator);

  }

  @Test
  void should_fail_if_iterator_is_not_exhausted() {
    // GIVEN
    List<String> list = List.of("Luke", "Yoda", "Vader");
    Iterator iterator = list.iterator();

    // WHEN
    iterator.next();
    AssertionError error = expectAssertionError(() -> iterators.assertIsExhausted(INFO, iterator));

    // THEN
    assertThat(error).hasMessage(shouldBeExhausted().create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Iterator iterator = null;

    // WHEN
    AssertionError error = expectAssertionError(() -> iterators.assertIsExhausted(INFO, iterator));

    // THEN
    assertThat(error).hasMessage(shouldNotBeNull().create());
  }
}
