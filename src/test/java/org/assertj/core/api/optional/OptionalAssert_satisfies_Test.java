package org.assertj.core.api.optional;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.error.OptionalShouldBePresent.shouldBePresent;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.Optional;

import org.assertj.core.api.BaseTest;
import org.junit.Test;

public class OptionalAssert_satisfies_Test extends BaseTest {
  @Test
  public void should_fail_when_optional_is_null() {
    thrown.expectAssertionError(actualIsNull());
    assertThat((Optional<String>) null).satisfies(s -> {
    });
  }

  @Test
  public void should_fail_when_optional_is_empty() {
    thrown.expectAssertionError(shouldBePresent(Optional.empty()).create());
    assertThat(Optional.empty()).satisfies(o -> {
    });
  }

  @Test
  public void should_pass_when_consumer_passes() {
    assertThat(Optional.of("something")).satisfies(s -> {
      assertThat(s).isEqualTo("something")
                   .startsWith("some")
                   .endsWith("thing");
    });
    assertThat(Optional.of(10)).satisfies(i -> {
      assertThat(i).isGreaterThan(9);
    });
  }

  @Test
  public void should_fail_from_consumer() {
    thrown.expectAssertionError("expected:<\"something[]\"> but was:<\"something[ else]\">");
    assertThat(Optional.of("something else")).satisfies(s -> {
      assertThat(s).isEqualTo("something");
    });
  }
}
