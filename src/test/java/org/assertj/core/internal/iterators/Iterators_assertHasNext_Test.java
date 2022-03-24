package org.assertj.core.internal.iterators;

import org.assertj.core.internal.IteratorsBaseTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.error.ShouldHaveNext.shouldHaveNext;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

public class Iterators_assertHasNext_Test extends IteratorsBaseTest {

  @Test
  void should_pass_if_iterator_has_next() {
    // GIVEN
    List<String> list = List.of("Luke", "Yoda", "Vader");
    Iterator iterator = list.iterator();
    // WHEN
    iterator.next();
    // THEN
    iterators.assertHasNext(info, iterator);

  }

  @Test
  void should_fail_if_iterator_has_no_next() {
    // GIVEN
    List<String> list = List.of("Luke");
    Iterator iterator = list.iterator();

    // WHEN
    iterator.next();
    AssertionError error = expectAssertionError(() -> iterators.assertHasNext(INFO, iterator));

    // THEN
    assertThat(error).hasMessage(shouldHaveNext().create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Iterator iterator = null;

    // WHEN
    AssertionError error = expectAssertionError(() -> iterators.assertHasNext(INFO, iterator));

    // THEN
    assertThat(error).hasMessage(shouldNotBeNull().create());
  }

}
