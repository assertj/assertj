package org.assertj.core.api.optional;

import org.assertj.core.api.BaseTest;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.OptionalShouldBePresent.shouldBePresent;
import static org.assertj.core.error.OptionalShouldContainInstanceOf.shouldContainInstanceOf;

public class OptionalAssert_containsInstanceOf_Test extends BaseTest {

  @Test
  public void should_fail_if_optional_is_empty() {
    Optional<Object> actual = Optional.empty();

    thrown.expectAssertionError(shouldBePresent(actual).create());

    assertThat(actual).containsInstanceOf(Object.class);
  }

  @Test
  public void should_pass_if_optional_contains_required_type() {
    assertThat(Optional.of("something")).containsInstanceOf(String.class);
  }

  @Test
  public void should_pass_if_optional_contains_required_type_subclass() {
    assertThat(Optional.of(new SubClass())).containsInstanceOf(ParentClass.class);
  }

  @Test
  public void should_fail_if_optional_contains_other_type_than_required() {
    Optional<ParentClass> actual = Optional.of(new ParentClass());

    thrown.expectAssertionError(shouldContainInstanceOf(actual, OtherClass.class).create());

    assertThat(actual).containsInstanceOf(OtherClass.class);
  }

  private static class ParentClass { }
  private static class SubClass extends ParentClass { }
  private static class OtherClass { }
}