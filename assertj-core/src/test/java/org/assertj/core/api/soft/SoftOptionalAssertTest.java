package org.assertj.core.api.soft;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.soft.SoftAssertFactories.STRING;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.GeneratedSoftAssertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.error.MultipleAssertionsError;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;

@SuppressWarnings("ResultOfMethodCallIgnored")
public final class SoftOptionalAssertTest {
  private final GeneratedSoftAssertions softly = new GeneratedSoftAssertions();

  @Test
  void should_gather_all_assertion_errors() {
    // GIVEN
    Optional<String> actual = Optional.of("foo");
    softly.assertThat(actual)
          .isPresent()
          .containsSame("bar")
          .containsSame("baz");
    // WHEN
    var multipleAssertionsError = expectMultipleAssertionsError(softly::assertAll);
    // THEN
    List<AssertionError> errors = multipleAssertionsError.getErrors();
    then(errors).hasSize(2);
    then(errors.get(0)).hasMessageContainingAll("to contain the instance", "bar");
    then(errors.get(1)).hasMessageContainingAll("to contain the instance", "baz");
  }

  @Test
  void should_support_and_delegate_to_non_assertion_methods() {
    // GIVEN
    Optional<String> actual = Optional.of("foo");
    softly.assertThat(actual)
          .usingValueComparator(String::compareToIgnoreCase)
          .contains("FOO")
          .contains("bar")
          .contains("baz");
    // WHEN
    var multipleAssertionsError = expectMultipleAssertionsError(softly::assertAll);
    // THEN
    List<AssertionError> errors = multipleAssertionsError.getErrors();
    then(errors).hasSize(2);
    then(errors.get(0)).hasMessageContainingAll("to contain", "bar");
    then(errors.get(1)).hasMessageContainingAll("to contain", "baz");
  }

  @Test
  void should_support_get_navigation_methods() {
    // GIVEN
    Optional<String> actual = Optional.of("foo");
    softly.assertThat(actual).get().isEqualTo("bar");
    // WHEN
    var multipleAssertionsError = expectMultipleAssertionsError(softly::assertAll);
    // THEN
    List<AssertionError> errors = multipleAssertionsError.getErrors();
    then(errors).hasSize(1);
    then(errors.get(0)).hasMessageContaining("bar");
  }

  @Test
  void should_support_strongly_typed_get_navigation_methods() {
    // GIVEN
    Optional<String> actual = Optional.of("foo");
    softly.assertThat(actual).get(STRING).isEmpty();
    // WHEN
    var multipleAssertionsError = expectMultipleAssertionsError(softly::assertAll);
    // THEN
    List<AssertionError> errors = multipleAssertionsError.getErrors();
    then(errors).hasSize(1);
    then(errors.get(0)).hasMessage(shouldBeEmpty("foo").create());
  }

  @Test
  void should_support_flatMap_navigation_methods() {
    // GIVEN
    Optional<String> actual = Optional.of("foo");
    softly.assertThat(actual).flatMap(s -> Optional.of(s.length())).contains(5);
    // WHEN
    var multipleAssertionsError = expectMultipleAssertionsError(softly::assertAll);
    // THEN
    List<AssertionError> errors = multipleAssertionsError.getErrors();
    then(errors).hasSize(1);
    then(errors.get(0)).hasMessageContainingAll("Optional[3]", "5");
  }

  private static @NonNull MultipleAssertionsError expectMultipleAssertionsError(ThrowingCallable callable) {
    return (MultipleAssertionsError) expectAssertionError(callable);
  }

}
