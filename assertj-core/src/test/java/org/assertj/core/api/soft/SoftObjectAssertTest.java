package org.assertj.core.api.soft;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.soft.SoftAssertFactories.STRING;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.util.AssertionsUtil.expectMultipleAssertionsError;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.GeneratedSoftAssertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("ResultOfMethodCallIgnored")
public final class SoftObjectAssertTest {
  private final GeneratedSoftAssertions softly = new GeneratedSoftAssertions();

  @Test
  void should_gather_all_assertion_errors() {
    // GIVEN
    Object actual = "foo";
    softly.assertThat(actual)
          .isEqualTo("foo")
          .isSameAs("bar")
          .hasToString("baz");
    // WHEN
    var multipleAssertionsError = expectMultipleAssertionsError(softly::assertAll);
    // THEN
    List<AssertionError> errors = multipleAssertionsError.getErrors();
    then(errors).hasSize(2);
    then(errors.get(0)).hasMessageContaining("bar");
    then(errors.get(1)).hasMessageContaining("baz");
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
  void should_support_strongly_typed_get_navigation_methods() {
    // GIVEN
    Object actual = "foo";
    softly.assertThat(actual).asInstanceOf(STRING).isEmpty();
    // WHEN
    var multipleAssertionsError = expectMultipleAssertionsError(softly::assertAll);
    // THEN
    List<AssertionError> errors = multipleAssertionsError.getErrors();
    then(errors).hasSize(1);
    then(errors.get(0)).hasMessage(shouldBeEmpty("foo").create());
  }

}
