package org.assertj.core.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

class DefaultAssertionErrorCollector_setAfterAssertionErrorCollected_Test {
  private List<String> stringList;
  private List<Throwable> throwableList;
  private SoftAssertions softly;

  @BeforeEach
  void given() {
    stringList = new ArrayList<>();
    throwableList = new ArrayList<>();
    softly = new SoftAssertions();
  }

  @Test
  void should_perform_both_specified_actions_on_each_assertion_error() {
    // WHEN
    softly.setAfterAssertionErrorCollected(err -> stringList.add(err.getMessage()));
    softly.setAfterAssertionErrorCollected(throwableList::add);
    softly.assertThat(1)
          .isEqualTo(1_000)
          .isBetween(7, 15)
          .isEven();
    // THEN
    then(stringList).hasSameSizeAs(throwableList).hasSize(3);
  }

  @Test
  void should_execute_both_specified_actions_on_each_manually_added_error() {
    // WHEN
    softly.setAfterAssertionErrorCollected(err -> stringList.add(err.getMessage()));
    softly.setAfterAssertionErrorCollected(throwableList::add);
    softly.collectAssertionError(new AssertionError("hello"));
    softly.collectAssertionError(new AssertionError("world"));
    // THEN
    then(stringList).hasSameSizeAs(throwableList).hasSize(2);
  }

  @Test
  void should_pass_if_the_callback_object_can_be_set_only_one_time() {
    // GIVEN
    AfterAssertionErrorCollected callback = throwableList::add;
    // WHEN
    for (int i = 0; i < 20; i++) {
      softly.setAfterAssertionErrorCollected(callback);
    }
    softly.collectAssertionError(new AssertionError("hello"));
    // THEN
    then(throwableList).hasSize(1);
  }
}
