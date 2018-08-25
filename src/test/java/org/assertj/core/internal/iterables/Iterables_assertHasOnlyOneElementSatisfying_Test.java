package org.assertj.core.internal.iterables;

import org.assertj.core.internal.IterablesBaseTest;
import org.assertj.core.test.Jedi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.error.ShouldHaveSize.shouldHaveSize;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;

class Iterables_assertHasOnlyOneElementSatisfying_Test extends IterablesBaseTest {

  private List<Jedi> actual;
  private Consumer<Jedi> consumer;

  @BeforeEach
  @Override
  public void setUp() {
    super.setUp();
    actual = newArrayList(new Jedi("Joda", "Green"));
    consumer = jedi -> assertThat(jedi.lightSaberColor).isEqualTo("Green");
  }

  @Test
  void should_pass_if_only_one_element_satisfies_condition() {
    iterables.assertHasOnlyOneElementSatisfying(info, actual, consumer);
  }

  @Test
  void should_throw_error_if_condition_is_null() {
    assertThatNullPointerException().isThrownBy(() ->
      iterables.assertHasOnlyOneElementSatisfying(info, actual, null)
    );
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(
      () -> iterables.assertHasOnlyOneElementSatisfying(info, null, consumer)
    ).withMessage(actualIsNull());
  }

  @Test
  void should_has_only_one_element() {
    actual.add(new Jedi("Luke", "Blue"));

    assertThatExceptionOfType(AssertionError.class).isThrownBy(
      () -> iterables.assertHasOnlyOneElementSatisfying(info, actual, null)
    ).withMessage(shouldHaveSize(actual, actual.size(), 1).create());
  }
}
